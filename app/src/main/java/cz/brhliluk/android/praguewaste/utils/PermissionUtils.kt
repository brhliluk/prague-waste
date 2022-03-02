package cz.brhliluk.android.praguewaste.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

fun Context.withPermission(permission: String, onDenied: (() -> Unit)? = null, onGranted: () -> Unit) = Dexter.withContext(this)
    .withPermission(permission)
    .withListener(object : PermissionListener {
        override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) { token.continuePermissionRequest() }
        override fun onPermissionDenied(response: PermissionDeniedResponse) { onDenied?.invoke() }
        override fun onPermissionGranted(response: PermissionGrantedResponse?) { onGranted() }
    }).check()

fun Context.hasPermissions(vararg permissions: String) = permissions.all {
    ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
}

val Context.hasLocationPermission get() =
    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED