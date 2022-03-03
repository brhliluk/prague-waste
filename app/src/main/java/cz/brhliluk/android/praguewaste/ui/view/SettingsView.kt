package cz.brhliluk.android.praguewaste.ui.view

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.brhliluk.android.praguewaste.R
import cz.brhliluk.android.praguewaste.model.Bin
import dev.burnoo.compose.rememberpreference.rememberBooleanPreference

@Composable
fun SettingsView() {
    var darkTheme by rememberBooleanPreference(
        keyName = "darkTheme",
        initialValue = isSystemInDarkTheme(),
        defaultValue = false
    )

    Scaffold {
        Column(
            Modifier
                .fillMaxSize()
                .padding(12.dp)) {
            Text(stringResource(R.string.app_name), fontSize = 25.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 10.dp))
            Row (verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(R.string.use_dark_mode))
                Spacer(modifier = Modifier.weight(1f))
                Switch(checked = darkTheme, onCheckedChange = { darkTheme = !darkTheme })
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