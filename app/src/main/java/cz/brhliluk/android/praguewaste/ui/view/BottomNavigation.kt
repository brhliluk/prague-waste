package cz.brhliluk.android.praguewaste.ui.view

import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cz.brhliluk.android.praguewaste.ui.activity.SettingsActivity
import cz.brhliluk.android.praguewaste.ui.theme.VeryLightGray
import cz.brhliluk.android.praguewaste.utils.onClick
import cz.brhliluk.android.praguewaste.viewmodel.BottomSheet
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel


@ExperimentalAnimationApi
@Composable
fun BottomNavIcon(icon: ImageVector, selectedIcon: ImageVector, modifier: Modifier, selected: Boolean, onIconClick: () -> Unit) {
    Box(
        modifier,
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = selected,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            Box(
                modifier = Modifier
                    .width(icon.viewportWidth.dp + 40.dp)
                    .height(icon.viewportHeight.dp + 20.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )
        }
        IconButton(
            onClick = onIconClick
        ) {
            Icon(
                imageVector = if (selected) selectedIcon else icon,
                contentDescription = null,
                tint = Color.Black,
            )
        }
    }

}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun BottomNavBar(vm: MainViewModel, sheetScaffoldState: BottomSheetScaffoldState) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Row(
        Modifier
            .fillMaxWidth()
            .background(color = VeryLightGray)
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavIcon(Icons.Outlined.Search, Icons.Filled.Search, Modifier.weight(1f), vm.activeBottomSheet == BottomSheet.SEARCH) {
            vm.activeBottomSheet = vm.activeBottomSheet.onClick(BottomSheet.SEARCH, coroutineScope, sheetScaffoldState)
        }
        BottomNavIcon(Icons.Outlined.LocationOn, Icons.Filled.LocationOn, Modifier.weight(1f), vm.activeBottomSheet == BottomSheet.NEAR) {
            vm.activeBottomSheet = vm.activeBottomSheet.onClick(BottomSheet.NEAR, coroutineScope, sheetScaffoldState)
        }
        BottomNavIcon(Icons.Outlined.Settings, Icons.Filled.Settings, Modifier.weight(1f), false) {
            context.startActivity(Intent(context, SettingsActivity::class.java))
        }
    }
}