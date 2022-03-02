package cz.brhliluk.android.praguewaste.ui.view

import android.os.Bundle
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.compose.*
import com.google.maps.android.ktx.awaitMap
import cz.brhliluk.android.praguewaste.R
import cz.brhliluk.android.praguewaste.model.Bin
import cz.brhliluk.android.praguewaste.utils.hasLocationPermission
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun GoogleMaps(vm: MainViewModel) {
    val mapView = rememberMapViewWithLifeCycle()
    val localContext = LocalContext.current

    val currentLocation by vm.location.collectAsState()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(
                vm.location.value.latitude,
                vm.location.value.longitude
            ), 15f
        )
    }

    val locationEnabled by remember { mutableStateOf(localContext.hasLocationPermission) }

    AndroidView({ mapView }) { map ->
        // Reading values so that AndroidView recomposes when it changes.
        val currentBins = vm.currentBins.value
        val locationEnabledLocal = locationEnabled

        CoroutineScope(Dispatchers.Main).launch {
            print("Recomposed locationEnabled: $locationEnabledLocal")
            //noinspection MissingPermission
            map.getMapAsync { it.isMyLocationEnabled = locationEnabledLocal }

            map.awaitMap().apply {
                val clusterManager = ClusterManager<Bin>(localContext, this)

                setOnCameraIdleListener(clusterManager)

                clusterManager.addItems(currentBins)

                clusterManager.cluster()
            }
        }
    }
}

@Composable
fun rememberMapViewWithLifeCycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            id = R.id.map_frame
        }
    }
    val lifeCycleObserver = rememberMapLifecycleObserver(mapView)
    val lifeCycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifeCycle) {
        lifeCycle.addObserver(lifeCycleObserver)
        onDispose { lifeCycle.removeObserver(lifeCycleObserver) }
    }

    return mapView
}

@Composable
fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    remember(mapView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> throw IllegalStateException()
            }
        }
    }