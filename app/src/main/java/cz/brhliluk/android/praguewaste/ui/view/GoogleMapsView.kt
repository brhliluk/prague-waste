package cz.brhliluk.android.praguewaste.ui.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import cz.brhliluk.android.praguewaste.utils.location
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel

@Composable
fun WasteGoogleMap(vm: MainViewModel) {

    val mapProperties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }
    val uiSettings by remember { mutableStateOf(MapUiSettings(myLocationButtonEnabled = true)) }

    val currentLocation by vm.location.collectAsState()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(
                vm.location.value.latitude,
                vm.location.value.longitude
            ), 15f
        )
    }

    val currentBins by vm.currentBins.collectAsState()

    GoogleMap(
        properties = mapProperties,
        uiSettings = uiSettings,
        cameraPositionState = cameraPositionState,
        modifier = Modifier.fillMaxSize()
    ) {
        currentBins.forEach { bin ->
            // TODO icon, onclick
            Marker(position = bin.location, title = bin.address)
        }
    }
}