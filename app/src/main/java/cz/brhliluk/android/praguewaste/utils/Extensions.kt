package cz.brhliluk.android.praguewaste.utils

import com.google.android.gms.maps.model.LatLng
import cz.brhliluk.android.praguewaste.model.Bin

val Bin.location: LatLng get() = LatLng(latitude, longitude)