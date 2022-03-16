package cz.brhliluk.android.praguewaste.ui.view

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import cz.brhliluk.android.praguewaste.ui.theme.PaperBlue
import cz.brhliluk.android.praguewaste.viewmodel.BottomSheet
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalMaterial3Api
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

    ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
        Scaffold(
            bottomBar = {
                Navigation(
                    Modifier
                        .navigationBarsWithImePadding()
                        .statusBarsPadding(),
                    vm,
                    scaffoldState
                )
            }
        ) {
            // TODO move search up by NavigationHeight
            // TODO move filter on tablet
            SheetView(vm, scaffoldState) {
                Box(Modifier.navigationBarsWithImePadding()) {
                    GoogleMaps(vm)
                    FloatingActionButton(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 90.dp, end = 12.dp)
                            .size(40.dp),
                        onClick = { vm.trashTypesFilterOpen = !vm.trashTypesFilterOpen },
                        backgroundColor = PaperBlue,
                        contentColor = Color.White
                    ) {
                        Icon(Icons.Filled.FilterAlt, "Filter icon")
                    }
                    TrashTypeFilterView(vm)
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
        }
    }
}