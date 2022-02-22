package cz.brhliluk.android.praguewaste.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cz.brhliluk.android.praguewaste.viewmodel.BottomSheet
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel

@ExperimentalMaterialApi
@Composable
fun MainView(vm: MainViewModel) {

    // TODO: state changes
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    Column(Modifier.fillMaxSize()) {
        ModalBottomSheetLayout(
            sheetContent = {
                when (vm.activeBottomSheet) {
                    BottomSheet.NONE -> Box(Modifier.defaultMinSize(minHeight = 1.dp)) // crashes with null passed
                    BottomSheet.SEARCH -> BottomSearchView(vm)
                    BottomSheet.NEAR -> BottomNearView(vm)
                }
            },
            sheetState = sheetState,
            modifier = Modifier.weight(1f)
        ) {
            GoogleMaps(vm)
        }
        BottomNavBar(vm, sheetState)
    }
}