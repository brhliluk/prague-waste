package cz.brhliluk.android.praguewaste.ui.view

import android.content.Intent
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import cz.brhliluk.android.praguewaste.R
import cz.brhliluk.android.praguewaste.ui.activity.SettingsActivity
import cz.brhliluk.android.praguewaste.utils.onClick
import cz.brhliluk.android.praguewaste.viewmodel.BottomSheet
import cz.brhliluk.android.praguewaste.viewmodel.MainViewModel

@ExperimentalMaterial3Api
@ExperimentalMaterialApi
@Composable
fun Navigation(modifier: Modifier, vm: MainViewModel, sheetScaffoldState: BottomSheetScaffoldState, drawerState: DrawerState) {
    val configuration = LocalConfiguration.current

    when (configuration.orientation) {
        ORIENTATION_LANDSCAPE -> SideNavigation(modifier, vm, drawerState)
        else -> BottomNavigation(modifier, vm, sheetScaffoldState)
    }
}

@ExperimentalMaterial3Api
@ExperimentalMaterialApi
@Composable
fun BottomNavigation(modifier: Modifier, vm: MainViewModel, sheetScaffoldState: BottomSheetScaffoldState) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    NavigationBar(modifier = modifier, contentColor = Color.Black, containerColor = Color.LightGray) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Search, contentDescription = null, tint = Color.Black) },
            label = { androidx.compose.material3.Text(stringResource(R.string.search)) },
            selected = vm.activeBottomSheet == BottomSheet.SEARCH,
            onClick = {
                vm.activeBottomSheet = vm.activeBottomSheet.onClick(BottomSheet.SEARCH, coroutineScope, sheetScaffoldState)
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    if (vm.activeBottomSheet == BottomSheet.NEAR) Icons.Filled.LocationOn else Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = Color.Black
                )
            },
            label = { androidx.compose.material3.Text(stringResource(R.string.near)) },
            selected = vm.activeBottomSheet == BottomSheet.NEAR,
            onClick = {
                vm.activeBottomSheet = vm.activeBottomSheet.onClick(BottomSheet.NEAR, coroutineScope, sheetScaffoldState)
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Settings, contentDescription = null, tint = Color.Black) },
            label = { androidx.compose.material3.Text(stringResource(R.string.settings)) },
            selected = false,
            onClick = {
                context.startActivity(Intent(context, SettingsActivity::class.java))
            }
        )
    }
}

@ExperimentalMaterial3Api
@ExperimentalMaterialApi
@Composable
fun SideNavigation(modifier: Modifier, vm: MainViewModel, drawerState: DrawerState) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    NavigationRail(modifier = modifier, contentColor = Color.Black, containerColor = Color.LightGray) {
        NavigationRailItem(
            icon = { Icon(Icons.Filled.Search, contentDescription = null, tint = Color.Black) },
            label = { androidx.compose.material3.Text(stringResource(R.string.search)) },
            selected = vm.activeBottomSheet == BottomSheet.SEARCH,
            onClick = {
                vm.activeBottomSheet = vm.activeBottomSheet.onClick(BottomSheet.SEARCH, coroutineScope, drawerState)
            }
        )
        NavigationRailItem(
            icon = {
                Icon(
                    if (vm.activeBottomSheet == BottomSheet.NEAR) Icons.Filled.LocationOn else Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = Color.Black
                )
            },
            label = { androidx.compose.material3.Text(stringResource(R.string.near)) },
            selected = vm.activeBottomSheet == BottomSheet.NEAR,
            onClick = {
                vm.activeBottomSheet = vm.activeBottomSheet.onClick(BottomSheet.NEAR, coroutineScope, drawerState)
            }
        )
        NavigationRailItem(
            icon = { Icon(Icons.Outlined.Settings, contentDescription = null, tint = Color.Black) },
            label = { androidx.compose.material3.Text(stringResource(R.string.settings)) },
            selected = false,
            onClick = {
                context.startActivity(Intent(context, SettingsActivity::class.java))
            }
        )
    }
}