package cz.brhliluk.android.praguewaste.utils

import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.DrawerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.MutableState
import com.google.android.gms.maps.model.LatLng
import cz.brhliluk.android.praguewaste.model.Bin
import cz.brhliluk.android.praguewaste.viewmodel.BottomSheet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
fun BottomSheet.onClick(clicked: BottomSheet, coroutineScope: CoroutineScope, sheetScaffoldState: BottomSheetScaffoldState): BottomSheet {
    val resultSheet = when (this) {
        BottomSheet.NONE -> clicked.instance
        // Search is shown
        BottomSheet.SEARCH -> {
            // Button clicked on
            when (clicked) {
                BottomSheet.NONE -> error("None is not part of bottom navigation")
                BottomSheet.SEARCH -> BottomSheet.NONE
                BottomSheet.NEAR -> BottomSheet.NEAR
            }
        }
        BottomSheet.NEAR -> {
            when (clicked) {
                BottomSheet.NONE -> error("None is not part of bottom navigation")
                BottomSheet.SEARCH -> BottomSheet.SEARCH
                BottomSheet.NEAR -> BottomSheet.NONE
            }
        }
    }
    when (resultSheet) {
        BottomSheet.NONE -> coroutineScope.launch { sheetScaffoldState.bottomSheetState.collapse() }
        BottomSheet.SEARCH, BottomSheet.NEAR -> coroutineScope.launch { sheetScaffoldState.bottomSheetState.expand() }
    }
    return resultSheet
}

@ExperimentalMaterialApi
fun BottomSheet.onClick(clicked: BottomSheet, coroutineScope: CoroutineScope, drawerState: DrawerState): BottomSheet {
    val resultSheet = when (this) {
        BottomSheet.NONE -> clicked.instance
        // Search is shown
        BottomSheet.SEARCH -> {
            // Button clicked on
            when (clicked) {
                BottomSheet.NONE -> error("None is not part of bottom navigation")
                BottomSheet.SEARCH -> BottomSheet.NONE
                BottomSheet.NEAR -> BottomSheet.NEAR
            }
        }
        BottomSheet.NEAR -> {
            when (clicked) {
                BottomSheet.NONE -> error("None is not part of bottom navigation")
                BottomSheet.SEARCH -> BottomSheet.SEARCH
                BottomSheet.NEAR -> BottomSheet.NONE
            }
        }
    }
    return resultSheet
}


val BottomSheet.instance: BottomSheet
    get() = when (this) {
        BottomSheet.NONE -> BottomSheet.NONE
        BottomSheet.SEARCH -> BottomSheet.SEARCH
        BottomSheet.NEAR -> BottomSheet.NEAR
    }

val Bin.offsetLocation: LatLng
    get() = LatLng(position.latitude-0.00012, position.longitude)

inline fun <T> MutableState<Boolean>.load(
    block: () -> T
) : T? {
    value = true
    try { return block() }
    finally {
        value = false
    }
}