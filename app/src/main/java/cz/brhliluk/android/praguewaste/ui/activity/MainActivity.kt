package cz.brhliluk.android.praguewaste.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.lifecycle.lifecycleScope
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import cz.brhliluk.android.praguewaste.R
import cz.brhliluk.android.praguewaste.ui.theme.ComposeMapsTheme
import cz.brhliluk.android.praguewaste.ui.view.MainView
import cz.brhliluk.android.praguewaste.utils.hasLocationPermission
import cz.brhliluk.android.praguewaste.utils.withPermission
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.time.Duration.Companion.seconds

@ExperimentalMaterialApi
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    private val vm: MainViewModel by viewModel()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupLocationUpdates()
        setMapsContent()
    }

    private fun setMapsContent() {
        vm.updateFilters()
        setContent { ComposeMapsTheme { Surface(color = MaterialTheme.colors.background) { MainView(vm) } } }
    }

    private var isRequestRequired = true
    override fun onResume() {
        super.onResume()
        lifecycleScope.launchWhenCreated {
            if (vm.isLocationEnabled() && isRequestRequired) {
                askForLocationPermission()
            }
        }
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

    private fun askForLocationPermission() {
        isRequestRequired = false
        applicationContext.withPermission(Manifest.permission.ACCESS_FINE_LOCATION,
            onDenied = {
                Log.d("Location permission:", "denied")
                if (vm.isLocationEnabled) {
                    askForLocationDialog()
                }
            },
            onGranted = {
                Log.d("Location permission:", "granted")
                if (hasLocationPermission) {
                    vm.setMyLocationEnabled()
                    startLocationUpdates()
                }
            }
        )
    }

    private var locationDialogShown = false
    private fun askForLocationDialog() {
        if (locationDialogShown) return
        MaterialDialog(this).show {
            Log.d("Location dialog:", "shown")
            locationDialogShown = true
            title(R.string.no_location_title)
            message(R.string.location_required_dialog)
            onDismiss { locationDialogShown = false }
            cancelable(false)
            positiveButton(R.string.turn_on) {
                resultLauncher.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                isRequestRequired = true
                locationDialogShown = false
            }
            negativeButton(R.string.location_cancel) {
                vm.setLocationEnabled(false)
                isRequestRequired = true
                locationDialogShown = false
            }
        }
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (hasLocationPermission) {
            startLocationUpdates()
        } else {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT)
        }
    }

}

