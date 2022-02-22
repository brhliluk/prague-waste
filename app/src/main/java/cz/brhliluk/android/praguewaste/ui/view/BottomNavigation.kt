package cz.brhliluk.android.praguewaste.ui.view

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import cz.brhliluk.android.praguewaste.R
import cz.brhliluk.android.praguewaste.ui.activity.SettingsActivity
import cz.brhliluk.android.praguewaste.utils.onClick
import cz.brhliluk.android.praguewaste.viewmodel.BottomSheet
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel

@ExperimentalMaterialApi
@Composable
fun BottomNavBar(vm: MainViewModel, sheetState: ModalBottomSheetState) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Row (Modifier
        .fillMaxWidth()
        .background(color = Color.LightGray)
    ) {
        IconButton(onClick = {
            vm.activeBottomSheet = vm.activeBottomSheet.onClick(BottomSheet.SEARCH, coroutineScope, sheetState)
        }, Modifier.weight(1f)) {
            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = null
            )
        }

        IconButton(onClick = {
            vm.activeBottomSheet = vm.activeBottomSheet.onClick(BottomSheet.NEAR, coroutineScope, sheetState)
        }, Modifier.weight(1f)) {
            Icon(
                painter = painterResource(R.drawable.ic_nearby),
                contentDescription = null
            )
        }

        IconButton(onClick = {
            context.startActivity(
                Intent(
                    context,
                    SettingsActivity::class.java
                )
            )
        }, Modifier.weight(1f)) {
            Icon(
                painter = painterResource(R.drawable.ic_settings),
                contentDescription = null
            )
        }
    }
}