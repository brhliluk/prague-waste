package cz.brhliluk.android.praguewaste.ui.view

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import dev.burnoo.compose.rememberpreference.rememberBooleanPreference

@Composable
fun SettingsView() {
    var darkTheme by rememberBooleanPreference(
        keyName = "darkTheme",
        initialValue = isSystemInDarkTheme(),
        defaultValue = false
    )

    Column(Modifier.fillMaxSize()) {
        Text(text = "Prague Waste")
        Text(text = "v 0.2")
        Row {
            Text(text = "Use dark mode:")
            Switch(checked = darkTheme, onCheckedChange = { darkTheme = !darkTheme })
        }
    }
}