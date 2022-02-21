package cz.brhliluk.android.praguewaste.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cz.brhliluk.android.praguewaste.viewmodel.BottomSheet
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainView(vm: MainViewModel) {

    ModalBottomSheetLayout(
        sheetContent = { when (vm.activeBottomSheet) {
            BottomSheet.NONE -> Box(Modifier.defaultMinSize(minHeight = 1.dp)) // crashes with null passed
            BottomSheet.SEARCH -> BottomSearchView(vm)
            BottomSheet.NEAR -> BottomNearView(vm)
        } }
    ) {
        Column(Modifier.fillMaxSize()) {
            GoogleMaps(vm, Modifier.weight(1f))
            BottomNavBar(vm)
        }
    }
}