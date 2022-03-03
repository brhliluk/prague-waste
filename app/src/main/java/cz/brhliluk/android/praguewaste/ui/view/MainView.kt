package cz.brhliluk.android.praguewaste.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cz.brhliluk.android.praguewaste.viewmodel.BottomSheet
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel

@ExperimentalMaterialApi
@Composable
fun MainView(vm: MainViewModel) {

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )

    Column(Modifier.fillMaxSize()) {
        BottomSheetScaffold(
            sheetContent = {
                when (vm.activeBottomSheet) {
                    BottomSheet.NONE -> Box(Modifier.defaultMinSize(minHeight = 1.dp)) // crashes with null passed
                    BottomSheet.SEARCH -> BottomSearchView(vm)
                    BottomSheet.NEAR -> BottomNearView(vm)
                }
            },
            sheetShape = RoundedCornerShape(50.dp, 50.dp),
            scaffoldState = scaffoldState ,
            modifier = Modifier.weight(1f),
            sheetPeekHeight = 0.dp,
        ) {
            Box {
                if (vm.loading.value) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                GoogleMaps(vm)
            }
        }
        BottomNavBar(vm, scaffoldState)
    }
}