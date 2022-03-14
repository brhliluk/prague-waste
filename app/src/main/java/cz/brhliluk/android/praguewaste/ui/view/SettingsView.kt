package cz.brhliluk.android.praguewaste.ui.view

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.brhliluk.android.praguewaste.R
import cz.brhliluk.android.praguewaste.model.Bin
import cz.brhliluk.android.praguewaste.viewmodel.SettingsViewModel

@ExperimentalMaterial3Api
@Composable
fun SettingsView(vm: SettingsViewModel) {

    val locationEnabled = vm.isLocationEnabledAsFlow().collectAsState(initial = true)
    val dispatcher = LocalOnBackPressedDispatcherOwner.current!!.onBackPressedDispatcher
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = remember(decayAnimationSpec) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
    }

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize(),
        topBar = {
            MediumTopAppBar(
                title = { Text(stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = { dispatcher.onBackPressed() }) {
                        Icon(Icons.Filled.ArrowBack, "backIcon", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color.DarkGray,
                    titleContentColor = Color.White,
                ),
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        Column(
            Modifier
                .padding(12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(stringResource(R.string.legend), color = Color.White, modifier = Modifier.padding(bottom = 12.dp), fontSize = 20.sp)
            Column(Modifier.padding(bottom = 12.dp)) {
                Bin.TrashType.all.forEach { trashType ->
                    TrashTypeItemView(trashType)
                }
            }

            Divider(Modifier.padding(bottom = 12.dp))
            Text(
                stringResource(R.string.location),
                color = Color.White,
                modifier = Modifier.padding(bottom = 6.dp),
                fontSize = 20.sp
            )
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 6.dp)) {
                Text(text = stringResource(R.string.use_location), color = Color.White)
                Spacer(modifier = Modifier.weight(1f))
                Switch(checked = locationEnabled.value, onCheckedChange = { vm.setLocationEnabled(it) })
            }
            Text(stringResource(R.string.prague_centre_default), modifier = Modifier.padding(bottom = 12.dp), color = Color.White)

            Divider(Modifier.padding(bottom = 12.dp))
            Text(
                stringResource(R.string.data_source),
                color = Color.White,
                modifier = Modifier.padding(bottom = 6.dp),
                fontSize = 20.sp
            )
            Text(stringResource(R.string.nominatim_attribution), modifier = Modifier.padding(bottom = 12.dp), color = Color.White)

            Divider(Modifier.padding(bottom = 12.dp))
            Text(
                stringResource(R.string.font_awesome),
                color = Color.White,
                modifier = Modifier.padding(bottom = 6.dp),
                fontSize = 20.sp
            )
            Text(stringResource(R.string.fontawesome_license), modifier = Modifier.padding(bottom = 12.dp), color = Color.White)
        }
    }
}