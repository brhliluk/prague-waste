package cz.brhliluk.android.praguewaste.ui.view

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ProvideWindowInsets
import cz.brhliluk.android.praguewaste.viewmodel.BottomSheet
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel

@ExperimentalMaterialApi
@Composable
fun SheetView(vm: MainViewModel, scaffoldState: BottomSheetScaffoldState, drawerState: DrawerState, content: @Composable () -> Unit) {
    val configuration = LocalConfiguration.current

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> SecondSideSheet(vm, content)
        else -> MainBottomSheetScaffold(vm, scaffoldState, content)
    }
}

@ExperimentalMaterialApi
@Composable
fun MainBottomSheetScaffold(vm: MainViewModel, scaffoldState: BottomSheetScaffoldState, content: @Composable () -> Unit) {
    BottomSheetScaffold(
        sheetContent = {
            when (vm.activeBottomSheet) {
                BottomSheet.NONE -> Box(Modifier.defaultMinSize(minHeight = 1.dp)) // crashes with null passed
                BottomSheet.SEARCH -> SearchView(vm)
                BottomSheet.NEAR -> NearView(vm)
            }
        },
        sheetShape = RoundedCornerShape(50.dp, 50.dp),
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
    ) {
        content()
    }
}

@Composable
fun MainSideSheet(vm: MainViewModel, drawerState: DrawerState, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalDrawer(
            drawerState = drawerState,
            gesturesEnabled = false,
            drawerContent = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    when (vm.activeBottomSheet) {
                        BottomSheet.NONE -> Box(Modifier.defaultMinSize(minHeight = 1.dp)) // crashes with null passed
                        BottomSheet.SEARCH -> SearchView(vm)
                        BottomSheet.NEAR -> NearView(vm)
                    }
                }
            },
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                content()
            }
        }
    }
}

@Composable
fun SecondSideSheet(vm: MainViewModel, content: @Composable () -> Unit) {
    ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
        Box {
            content()
            Column(Modifier.align(Alignment.TopEnd).background(Color.Gray).statusBarsPadding()) {
                when (vm.activeBottomSheet) {
                    BottomSheet.NONE -> {}
                    BottomSheet.SEARCH -> AnimatedVisibility(
                        visible = vm.activeBottomSheet == BottomSheet.SEARCH,
                        enter = slideInHorizontally(initialOffsetX = { -300 },),
                        exit = slideOutHorizontally(targetOffsetX = { -300 },)
                    ) { SearchView(vm) }

                    BottomSheet.NEAR -> if (vm.activeBottomSheet == BottomSheet.NEAR) {
                        NearView(vm)
                    }
                }
            }
        }
    }
}