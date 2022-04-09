package cz.brhliluk.android.praguewaste.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.ktx.awaitAnimateCamera
import com.google.maps.android.ktx.awaitMap
import cz.brhliluk.android.praguewaste.R
import cz.brhliluk.android.praguewaste.common.model.Bin
import cz.brhliluk.android.praguewaste.common.utils.LocationViewModel
import cz.brhliluk.android.praguewaste.common.utils.PreferencesManager
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GoogleMapsHelper(private val context: Context) : KoinComponent {
    private val infoWindowAdapter: InfoWindowAdapter by inject()
    private val preferencesManager: PreferencesManager by inject()

    private lateinit var clusterManager: ClusterManager<Bin>
    private lateinit var map: GoogleMap
    private var mapViewHashCode = 0

    suspend fun initGoogleMaps(mapView: MapView) {
        // No unnecessary new initializations on recomposition
        if (mapView.hashCode() == mapViewHashCode) return
        mapViewHashCode = mapView.hashCode()
        map = mapView.awaitMap()
        map.setPadding(0, 100, 0, 200)
        clusterManager = ClusterManager(context, map)
        map.setOnCameraIdleListener(clusterManager)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LocationViewModel.centrePrague, 10f))
        // Start navigation
        clusterManager.markerCollection.setOnInfoWindowClickListener { marker ->
            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=${marker.position.latitude},${marker.position.longitude}"))
            mapIntent.setPackage("com.google.android.apps.maps")
            mapIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            if (mapIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(mapIntent)
            }
        }
        clusterManager.markerCollection.setInfoWindowAdapter(infoWindowAdapter)
        clusterManager.setOnClusterClickListener {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(it.position, map.cameraPosition.zoom + 1), 500, null)
            true
        }
    }

    suspend fun setMyLocationEnabled() {
        if (this::map.isInitialized)
            //noinspection MissingPermission
            map.isMyLocationEnabled = preferencesManager.getLocationEnabled() && preferencesManager.hasLocationPermission
    }

    suspend fun selectBin(bin: Bin) {
        map.awaitAnimateCamera(CameraUpdateFactory.newLatLngZoom(bin.offsetLocation, 20.0f), 1000)
        // Give clusterManager time to load in all the markers if too far away
        delay(200)
        clusterManager.markerCollection.markers.find { it.position == bin.position }?.showInfoWindow()
    }

    fun replaceItems(bins: List<Bin>) {
        clusterManager.clearItems()
        clusterManager.addItems(bins)
        clusterManager.cluster()
    }
}