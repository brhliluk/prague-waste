package cz.brhliluk.android.praguewaste.common.utils

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow

interface LocationViewModel {
    val location: MutableStateFlow<LatLng>

    companion object {
        val centrePrague = LatLng(50.073658, 14.418540)
    }
}