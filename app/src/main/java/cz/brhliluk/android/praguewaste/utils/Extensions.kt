package cz.brhliluk.android.praguewaste.utils

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import com.google.android.gms.maps.model.LatLng
import cz.brhliluk.android.praguewaste.model.Bin
import cz.brhliluk.android.praguewaste.viewmodel.BottomSheet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

val Bin.location: LatLng get() = LatLng(latitude, longitude)

@ExperimentalMaterialApi
// TODO: ModalBottomSheet can get hidden itself - can result in double times hide
fun BottomSheet.onClick(clicked: BottomSheet, coroutineScope: CoroutineScope, sheetState: ModalBottomSheetState): BottomSheet {
    val resultSheet = when (this) {
        BottomSheet.NONE -> clicked.instance
        // Search is shown
        BottomSheet.SEARCH -> {
            // Button clicked on
            when (clicked) {
                BottomSheet.NONE -> throw IllegalStateException("None is not part of bottom navigation")
                BottomSheet.SEARCH -> BottomSheet.NONE
                BottomSheet.NEAR -> BottomSheet.NEAR
            }
        }
        BottomSheet.NEAR -> {
            when (clicked) {
                BottomSheet.NONE -> throw IllegalStateException("None is not part of bottom navigation")
                BottomSheet.SEARCH -> BottomSheet.SEARCH
                BottomSheet.NEAR -> BottomSheet.NONE
            }
        }
    }
    when (resultSheet) {
        BottomSheet.NONE -> coroutineScope.launch { sheetState.hide() }
        BottomSheet.SEARCH, BottomSheet.NEAR -> coroutineScope.launch { sheetState.show() }
    }
    return resultSheet
}

val BottomSheet.instance: BottomSheet
    get() = when (this) {
        BottomSheet.NONE -> BottomSheet.NONE
        BottomSheet.SEARCH -> BottomSheet.SEARCH
        BottomSheet.NEAR -> BottomSheet.NEAR
    }