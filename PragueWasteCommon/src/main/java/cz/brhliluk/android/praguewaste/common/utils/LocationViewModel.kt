package cz.brhliluk.android.praguewaste.common.utils

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow

interface LocationViewModel {
    val location: MutableStateFlow<LatLng>

    fun saveLocation(loc: Location) {
        location.value = LatLng(loc.latitude, loc.longitude)
    }

    companion object {
        val centrePrague = LatLng(50.073658, 14.418540)
    }
}