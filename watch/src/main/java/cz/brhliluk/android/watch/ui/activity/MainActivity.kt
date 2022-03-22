package cz.brhliluk.android.watch.ui.activity

import android.Manifest
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import cz.brhliluk.android.praguewaste.common.utils.LocationHelper
import cz.brhliluk.android.praguewaste.common.utils.hasLocationPermission
import cz.brhliluk.android.praguewaste.common.utils.withPermission
import cz.brhliluk.android.watch.R
import cz.brhliluk.android.watch.ui.theme.PragueWasteTheme
import cz.brhliluk.android.watch.ui.view.MainView
import cz.brhliluk.android.watch.viewmodel.MainViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {
    private val vm: MainViewModel by viewModel()
    private val locationHelper: LocationHelper by inject { parametersOf({ location: Location -> vm.saveLocation(location) }) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationHelper.setupLocationUpdates()
        setContent {
            PragueWasteTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    MainView()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        locationHelper.stopLocationUpdates()
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

    private fun askForLocationPermission() {
        isRequestRequired = false
        applicationContext.withPermission(
            Manifest.permission.ACCESS_FINE_LOCATION,
            onDenied = {
                Toast.makeText(this, getString(R.string.prague_centre_reference), Toast.LENGTH_SHORT).show()
            },
            onGranted = { if (hasLocationPermission) locationHelper.startLocationUpdates() }
        )
    }

    companion object {
        const val BIN_EXTRA = "cz.brhliluk.android.praguewaste.BIN_MESSAGE"
        const val LOCATION_EXTRA = "cz.brhliluk.android.praguewaste.LOCATION_MESSAGE"
    }
}