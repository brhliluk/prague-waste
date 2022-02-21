package cz.brhliluk.android.praguewaste.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import cz.brhliluk.android.praguewaste.R
import cz.brhliluk.android.praguewaste.ui.theme.ComposeMapsTheme
import cz.brhliluk.android.praguewaste.ui.view.MainView
import cz.brhliluk.android.praguewaste.utils.hasPermissions
import cz.brhliluk.android.praguewaste.utils.withPermission
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.time.Duration.Companion.seconds

class MainActivity : ComponentActivity() {

    private val vm: MainViewModel by viewModel()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupLocationUpdates()
        applicationContext.withPermission(Manifest.permission.ACCESS_FINE_LOCATION,
            onDenied = { showLocationRequiredDialog() },
            onGranted = {
                if (hasLocationAccess) {
                    startLocationUpdates()
                    setMapsContent()
                } else showLocationRequiredDialog()
            }
        )
    }

    private fun setMapsContent() {
        vm.getNearestBins()
        setContent {
            ComposeMapsTheme {
                // A surface container using the 'background' color from the theme
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
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
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
                        } else settingsResult.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                )
            }
        }
    }

    private val hasLocationAccess get() = this@MainActivity.hasPermissions(Manifest.permission.ACCESS_FINE_LOCATION)

    private val settingsResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (hasLocationAccess) startLocationUpdates()
            else showLocationRequiredDialog()
        }
}

