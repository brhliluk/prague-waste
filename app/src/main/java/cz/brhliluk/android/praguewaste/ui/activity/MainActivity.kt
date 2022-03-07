package cz.brhliluk.android.praguewaste.ui.activity

import android.Manifest
import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import cz.brhliluk.android.praguewaste.ui.theme.ComposeMapsTheme
import cz.brhliluk.android.praguewaste.ui.view.MainView
import cz.brhliluk.android.praguewaste.utils.*
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.time.Duration.Companion.seconds

@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {

    private val vm: MainViewModel by viewModel()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            setupLocationUpdates()
            applicationContext.withPermission(Manifest.permission.ACCESS_FINE_LOCATION,
//                onDenied = { launch { setLocationEnabled(false) } },
                onGranted = { if (hasLocationPermission) {
//                    launch { setLocationEnabled(true) }
                    startLocationUpdates()
                } }
            )
            setMapsContent()
        }
    }

    private fun setMapsContent() {
        vm.fetchAllBins()
        setContent {
            ComposeMapsTheme {
                Surface(color = MaterialTheme.colors.background) {
                    MainView(vm)
                }
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

    private fun setupLocationUpdates() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create().apply {
            interval = 5.seconds.inWholeMilliseconds
            fastestInterval = 0.5.seconds.inWholeMilliseconds
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            smallestDisplacement = 10f // 10m
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult.locations.isNotEmpty()) {
                    // get latest location
                    val location = locationResult.lastLocation
                    vm.location.value = LatLng(location.latitude, location.longitude)
                }
            }
        }
    }

    private fun startLocationUpdates() {
        //noinspection MissingPermission
        if (hasLocationPermission) fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }
}

