package cz.brhliluk.android.praguewaste.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.gms.location.*
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import cz.brhliluk.android.praguewaste.R
import cz.brhliluk.android.praguewaste.ui.theme.ComposeMapsTheme
import cz.brhliluk.android.praguewaste.utils.hasPermissions
import cz.brhliluk.android.praguewaste.utils.withPermission
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.time.Duration.Companion.seconds

class MainActivity : ComponentActivity() {

    private val vm: MainViewModel by viewModel()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private val hasLocationAccess get() = this@MainActivity.hasPermissions(Manifest.permission.ACCESS_FINE_LOCATION)

    private val settingsResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (hasLocationAccess) startLocationUpdates()
        else showLocationRequiredDialog()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getLocationUpdates()
        applicationContext.withPermission(Manifest.permission.ACCESS_FINE_LOCATION,
            onDenied = { showLocationRequiredDialog() },
            onGranted = {
                if (hasLocationAccess) {
                    startLocationUpdates()
                    setMapsContent()
                }
                else showLocationRequiredDialog()
            }
        )
    }

    private fun setMapsContent() = setContent {
        ComposeMapsTheme {
            // A surface container using the 'background' color from the theme
            Surface(color = MaterialTheme.colors.background) {
                GoogleMaps()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun getLocationUpdates() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create().apply {
            interval = 5.seconds.inWholeMilliseconds
            fastestInterval = 0.5.seconds.inWholeMilliseconds
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            maxWaitTime = 10.seconds.inWholeMilliseconds
            smallestDisplacement = 170f // 170m
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult.locations.isNotEmpty()) {
                    // get latest location
                    val location = locationResult.lastLocation
                    vm.currentUserLocation.value = LatLng(location.latitude, location.longitude)
                }
            }
        }
    }

    private fun startLocationUpdates() {
        //noinspection MissingPermission
        fusedLocationClient.requestLocationUpdates(locationRequest,
            locationCallback,
            Looper.getMainLooper())
    }

    private fun showLocationRequiredDialog() {
        MaterialDialog(this).show {
            title(R.string.no_location_title)
            message(R.string.location_required_dialog)
            cancelable(false)
            negativeButton(R.string.exit) { finishAffinity() }
            positiveButton(R.string.turn_on) {
                this@MainActivity.withPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                    onDenied = { showLocationRequiredDialog() },
                    onGranted = {
                        if (this@MainActivity.hasPermissions(Manifest.permission.ACCESS_FINE_LOCATION)) {
                            startLocationUpdates()
                            setMapsContent()
                        }
                        else settingsResult.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                )
            }
        }
    }
}

@Composable
fun GoogleMaps() {
    val mapView = rememberMapViewWithLifeCycle()
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            AndroidView({ mapView }) { mapView ->
                CoroutineScope(Dispatchers.Main).launch {
                    //noinspection MissingPermission
                    mapView.getMapAsync {
                        it.mapType = 1
                        it.uiSettings.isZoomControlsEnabled = true
                        it.uiSettings.isMyLocationButtonEnabled = true
                        it.isMyLocationEnabled = true
                    }
                }
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

