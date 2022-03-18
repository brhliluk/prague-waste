package cz.brhliluk.android.praguewaste.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.brhliluk.android.praguewaste.common.utils.PreferencesManager
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SettingsViewModel : ViewModel(), KoinComponent {
    private val preferencesManager: PreferencesManager by inject()

    private var locationEnabled: Boolean? = null

    fun setLocationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            locationEnabled = preferencesManager.getLocationEnabled()
            preferencesManager.setLocationEnabled(enabled)
            locationEnabled = preferencesManager.getLocationEnabled()
        }
    }

    fun isLocationEnabledAsFlow() = preferencesManager.getLocationEnabledAsFlow()
}