package cz.brhliluk.android.praguewaste.utils

import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.MutableState
import cz.brhliluk.android.praguewaste.viewmodel.BottomSheet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

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

@ExperimentalMaterialApi
// TODO: ModalBottomSheet can get hidden itself - can result in double times hide
fun BottomSheet.onClick(clicked: BottomSheet, coroutineScope: CoroutineScope, sheetScaffoldState: BottomSheetScaffoldState): BottomSheet {
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
        BottomSheet.NONE -> coroutineScope.launch { sheetScaffoldState.bottomSheetState.collapse() }
        BottomSheet.SEARCH, BottomSheet.NEAR -> coroutineScope.launch { sheetScaffoldState.bottomSheetState.expand() }
    }
    return resultSheet
}

val BottomSheet.instance: BottomSheet
    get() = when (this) {
        BottomSheet.NONE -> BottomSheet.NONE
        BottomSheet.SEARCH -> BottomSheet.SEARCH
        BottomSheet.NEAR -> BottomSheet.NEAR
    }

inline fun <T> MutableState<Boolean>.load(
    block: () -> T
) : T? {
    value = true
    try { return block() }
    finally {
        value = false
    }
}