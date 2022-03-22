package cz.brhliluk.android.praguewaste.ui.activity

import android.Manifest
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.google.accompanist.insets.ProvideWindowInsets
import cz.brhliluk.android.praguewaste.R
import cz.brhliluk.android.praguewaste.common.utils.LocationHelper
import cz.brhliluk.android.praguewaste.common.utils.hasLocationPermission
import cz.brhliluk.android.praguewaste.common.utils.withPermission
import cz.brhliluk.android.praguewaste.ui.theme.ComposeMapsTheme
import cz.brhliluk.android.praguewaste.ui.view.MainView
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {

    private val vm: MainViewModel by viewModel()
    private val locationHelper: LocationHelper by inject { parametersOf({ location: Location -> vm.saveLocation(location) }) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        locationHelper.setupLocationUpdates()
        setMapsContent()
    }

    private fun setMapsContent() {
        vm.updateFilters()
        setContent {
            ProvideWindowInsets {
                ComposeMapsTheme { Surface(color = MaterialTheme.colors.background) { MainView() } }
            }
        }
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
        locationHelper.stopLocationUpdates()
    }

    private fun askForLocationPermission() {
        isRequestRequired = false
        applicationContext.withPermission(Manifest.permission.ACCESS_FINE_LOCATION,
            onDenied = {
                if (vm.isLocationEnabled) {
                    askForLocationDialog()
                }
            },
            onGranted = {
                if (hasLocationPermission) {
                    vm.setMyLocationEnabled()
                    locationHelper.startLocationUpdates()
                }
            }
        )
    }

    private var locationDialogShown = false
    private fun askForLocationDialog() {
        if (locationDialogShown) return
        MaterialDialog(this).show {
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
            locationHelper.startLocationUpdates()
        } else {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show()
        }
    }

}

