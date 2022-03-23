package cz.brhliluk.android.praguewaste.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import cz.brhliluk.android.praguewaste.ui.theme.PaperBlue
import cz.brhliluk.android.praguewaste.viewmodel.BottomSheet
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel
import org.koin.androidx.compose.getViewModel

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
fun MainView(vm: MainViewModel = getViewModel()) {
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
                    scaffoldState,
                )
            }
        ) {
            SheetView(scaffoldState) {
                Box(Modifier.navigationBarsWithImePadding()) {
                    GoogleMaps()
                    RotatingFabIconView(Modifier.align(TopEnd)) { vm.trashTypesFilterOpen = !vm.trashTypesFilterOpen }
                    TrashTypeFilterView()
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