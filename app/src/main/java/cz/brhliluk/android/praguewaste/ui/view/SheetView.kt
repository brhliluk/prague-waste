package cz.brhliluk.android.praguewaste.ui.view

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ProvideWindowInsets
import cz.brhliluk.android.praguewaste.viewmodel.BottomSheet
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel

@ExperimentalMaterialApi
@Composable
fun SheetView(vm: MainViewModel, scaffoldState: BottomSheetScaffoldState, content: @Composable () -> Unit) {
    val configuration = LocalConfiguration.current

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> MainSideSheet(vm, content)
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
fun MainSideSheet(vm: MainViewModel, content: @Composable () -> Unit) {
    ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
        Box {
            content()
            Column(Modifier.align(Alignment.TopEnd).background(Color.LightGray)) {
                AnimatedVisibility(
                    visible = vm.activeBottomSheet == BottomSheet.SEARCH || vm.activeBottomSheet == BottomSheet.NEAR,
                    enter = slideInHorizontally(
                        initialOffsetX = { 300 }, // small slide 300px
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearEasing // interpolator
                        )
                    ),
                    exit = slideOutHorizontally(
                        targetOffsetX = { 300 }, // small slide 300px
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearEasing // interpolator
                        )
                    )
                ) {
                    Column(
                        Modifier
                            .fillMaxHeight()
                            .statusBarsPadding()
                    ) {
                        when (vm.activeBottomSheet) {
                            BottomSheet.NONE -> {}
                            BottomSheet.SEARCH -> SearchView(vm)
                            BottomSheet.NEAR -> NearView(vm)
                        }
                    }
                }
            }

        }
    }
}