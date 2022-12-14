package cz.brhliluk.android.praguewaste.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import cz.brhliluk.android.praguewaste.ui.theme.ComposeMapsTheme
import cz.brhliluk.android.praguewaste.ui.view.SettingsView
import cz.brhliluk.android.praguewaste.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalMaterial3Api
class SettingsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeMapsTheme {
                SettingsView()
            }
        }
    }
}