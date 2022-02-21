package cz.brhliluk.android.praguewaste.ui.view

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import cz.brhliluk.android.praguewaste.R
import cz.brhliluk.android.praguewaste.ui.activity.SettingsActivity
import cz.brhliluk.android.praguewaste.viewmodel.BottomSheet
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel

@Composable
fun BottomNavBar(vm: MainViewModel) {
    val context = LocalContext.current

    Row (Modifier
        .fillMaxWidth()
        .background(color = Color.LightGray)
    ) {
        IconButton(onClick = {
            vm.activeBottomSheet = when (vm.activeBottomSheet) {
                BottomSheet.SEARCH -> BottomSheet.NONE
                BottomSheet.NONE, BottomSheet.NEAR -> BottomSheet.SEARCH
            }
        }, Modifier.weight(1f)) {
            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = null
            )
        }

        IconButton(onClick = {
            vm.activeBottomSheet = when (vm.activeBottomSheet) {
                BottomSheet.NEAR -> BottomSheet.NONE
                BottomSheet.NONE, BottomSheet.SEARCH -> BottomSheet.NEAR
            }
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