package cz.brhliluk.android.praguewaste.common.utils

import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import org.koin.core.component.KoinComponent
import kotlin.time.Duration.Companion.seconds

/**
 * Utility class to help saving current location
 * @param context context
 * @param saveLocation function/lambda that saves location to desired place
 */
class LocationHelper(val context: Context, val saveLocation: (Location) -> Unit) : KoinComponent {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    fun setupLocationUpdates() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        locationRequest = LocationRequest.create().apply {
            interval = 10.seconds.inWholeMilliseconds
            fastestInterval = 0.5.seconds.inWholeMilliseconds
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            smallestDisplacement = 10f // 10m
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult.locations.isNotEmpty()) {
                    // get latest location
                    val location = locationResult.lastLocation
                    saveLocation(location)
                }
            }
        }
    }

    fun startLocationUpdates() {
        //noinspection MissingPermission
        if (context.hasLocationPermission) fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun stopLocationUpdates() = fusedLocationClient.removeLocationUpdates(locationCallback)
}