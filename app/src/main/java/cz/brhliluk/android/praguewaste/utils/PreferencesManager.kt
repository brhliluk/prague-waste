package cz.brhliluk.android.praguewaste.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import cz.brhliluk.android.praguewaste.model.Bin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent

class PreferencesManager(private val context: Context) : KoinComponent {

    private fun <T> getKeyAsFlow(key: Preferences.Key<T>, default: T): Flow<T> =
        context.settingsDatastore.data.map { preferences ->
            preferences[key] ?: default
        }

    private suspend fun <T> getKey(key: Preferences.Key<T>, default: T): T {
        val preferences = context.settingsDatastore.data.first()
        return preferences[key] ?: default
    }

    private suspend fun <T> setKey(key: Preferences.Key<T>, value: T) {
        context.settingsDatastore.edit { settings ->
            settings[key] = value
        }
    }

    fun getTrashTypeEnabledAsFlow(trashType: Bin.TrashType) = getKeyAsFlow(booleanPreferencesKey(trashType.name), true)
    suspend fun getTrashTypeEnabled(trashType: Bin.TrashType) = getKey(booleanPreferencesKey(trashType.name), true)
    suspend fun getTrashTypeEnabled(trashTypes: List<Bin.TrashType>) = trashTypes.filter { getTrashTypeEnabled(it) }
    suspend fun setTrashType(trashType: Bin.TrashType, enabled: Boolean) = setKey(booleanPreferencesKey(trashType.name), enabled)

    fun getUseAllEnabledAsFlow() = getKeyAsFlow(allRequiredKey, true)
    suspend fun getUseAllEnabled() = getKey(allRequiredKey, true)
    suspend fun setUseAll(enabled: Boolean) = setKey(allRequiredKey, enabled)

    fun getLocationEnabledAsFlow() = getKeyAsFlow(locationEnabledKey, true)
    suspend fun getLocationEnabled() = getKey(locationEnabledKey, true)
    suspend fun setLocationEnabled(enabled: Boolean) = setKey(locationEnabledKey, enabled)

    val hasLocationPermission get() =
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    companion object {
        private val Context.settingsDatastore by preferencesDataStore(name = "settings")
        val allRequiredKey = booleanPreferencesKey("all_required")
        val locationEnabledKey = booleanPreferencesKey("location_enabled")
    }
}