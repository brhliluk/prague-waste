package cz.brhliluk.android.praguewaste.ui.view

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.brhliluk.android.praguewaste.R
import cz.brhliluk.android.praguewaste.model.Bin
import cz.brhliluk.android.praguewaste.viewmodel.SettingsViewModel

@Composable
// TODO: add nominatim mention
// TODO: add reference to Prague center
// TODO: add fontAwesome mention
fun SettingsView(vm: SettingsViewModel) {

    val locationEnabled = vm.isLocationEnabledAsFlow().collectAsState(initial = true)
    val dispatcher = LocalOnBackPressedDispatcherOwner.current!!.onBackPressedDispatcher

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.settings)) }, navigationIcon = {
                IconButton(onClick = { dispatcher.onBackPressed() }) {
                    Icon(Icons.Filled.ArrowBack, "backIcon")
                }
            })
        }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Text(
                stringResource(R.string.app_name),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 10.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(R.string.use_location))
                Spacer(modifier = Modifier.weight(1f))
                Switch(checked = locationEnabled.value, onCheckedChange = { vm.setLocationEnabled(it) })
            }
            Text(stringResource(R.string.legend))
            LazyColumn {
                items(items = Bin.TrashType.all, itemContent = { trashType ->
                    TrashTypeItemView(trashType)
                })
            }
        }
    }
}