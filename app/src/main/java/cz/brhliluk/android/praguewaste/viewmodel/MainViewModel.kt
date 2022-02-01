package cz.brhliluk.android.praguewaste.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class MainViewModel : ViewModel() {
    var currentUserLocation = MutableLiveData(LatLng(0.0, 0.0))
}