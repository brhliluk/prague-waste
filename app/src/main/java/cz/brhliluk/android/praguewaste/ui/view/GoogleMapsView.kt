package cz.brhliluk.android.praguewaste.ui.view

import android.os.Bundle
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.gms.maps.MapView
import cz.brhliluk.android.praguewaste.R
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Composable
fun GoogleMaps(vm: MainViewModel = getViewModel()) {
    val mapView = rememberMapViewWithLifeCycle()
    val localContext = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val locationEnabled by vm.isLocationEnabledFlow().collectAsState(initial = false)

    AndroidView({ mapView }) {
        val currentBins = vm.currentBins.value
        val localLocationEnabled = locationEnabled

        coroutineScope.launch {
            vm.initGoogleMaps(mapView)
            vm.setMyLocationEnabled()
            vm.replaceBins()
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