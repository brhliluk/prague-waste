package cz.brhliluk.android.praguewaste.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cz.brhliluk.android.praguewaste.viewmodel.BottomSheet
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel

@ExperimentalMaterialApi
@Composable
fun MainView(vm: MainViewModel) {

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Collapsed,
            confirmStateChange = {
                if (it == BottomSheetValue.Collapsed) vm.activeBottomSheet = BottomSheet.NONE
                true
            }
        )
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
            scaffoldState = scaffoldState,
            modifier = Modifier.weight(1f),
            sheetPeekHeight = 0.dp,
        ) {
            Box {
                GoogleMaps(vm)
                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 90.dp, end = 12.dp)
                        .size(40.dp),
                    onClick = { vm.trashTypesFilterOpen = !vm.trashTypesFilterOpen },
                    backgroundColor = Color.Blue, // TODO: colors
                    contentColor = Color.White // TODO: colors
                ) {
                    Icon(Icons.Filled.FilterAlt, "Filter icon")
                }
                TrashTypeFilterView(vm = vm)
                if (vm.loading.value) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
        BottomNavBar(vm, scaffoldState)
    }
}