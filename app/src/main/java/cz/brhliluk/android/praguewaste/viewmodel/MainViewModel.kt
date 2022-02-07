package cz.brhliluk.android.praguewaste.viewmodel

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel : ViewModel() {

    private val centrePrague = LatLng(50.073658, 14.418540)
    val location = MutableStateFlow(centrePrague)

}