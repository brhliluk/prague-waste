package cz.brhliluk.android.praguewaste.ui.view

import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cz.brhliluk.android.praguewaste.R
import cz.brhliluk.android.praguewaste.ui.activity.SettingsActivity
import cz.brhliluk.android.praguewaste.ui.theme.VeryLightGray
import cz.brhliluk.android.praguewaste.utils.onClick
import cz.brhliluk.android.praguewaste.viewmodel.BottomSheet
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel


@Composable
fun BottomNavIcon(@DrawableRes icon: Int, modifier: Modifier, onIconClick: () -> Unit) {
    IconButton(
        onClick = onIconClick,
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = Color.Black
        )
    }
}

@ExperimentalMaterialApi
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
        BottomNavIcon(R.drawable.ic_search, Modifier.weight(1f)) {
            vm.activeBottomSheet = vm.activeBottomSheet.onClick(BottomSheet.SEARCH, coroutineScope, sheetScaffoldState)
        }
        BottomNavIcon(R.drawable.ic_nearby, Modifier.weight(1f)) {
            vm.activeBottomSheet = vm.activeBottomSheet.onClick(BottomSheet.NEAR, coroutineScope, sheetScaffoldState)
        }
        BottomNavIcon(R.drawable.ic_settings, Modifier.weight(1f)) {
            context.startActivity(Intent(context, SettingsActivity::class.java))
        }
    }
}