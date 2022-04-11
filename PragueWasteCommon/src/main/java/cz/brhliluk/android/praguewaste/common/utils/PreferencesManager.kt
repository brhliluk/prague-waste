package cz.brhliluk.android.praguewaste.common.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import cz.brhliluk.android.praguewaste.common.model.Bin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent

/**
 * Utility class to help getting and saving key-value preferences using Preferences DataStore
 */
class PreferencesManager(private val context: Context) : KoinComponent {

    private val Context.settingsDatastore by preferencesDataStore(name = "settings")

    /**
     * Retrieves preferences value from DataStore as Flow
     * @param key Preferences Key
     * @param default value to be retrieved on non-existing preference
     * @return Flow of values corresponding to given key
     */
    private fun <T> getPrefAsFlow(key: Preferences.Key<T>, default: T): Flow<T> =
        context.settingsDatastore.data.map { preferences ->
            preferences[key] ?: default
        }

    /**
     * Retrieves preferences value from DataStore
     * @param key Preferences Key
     * @param default value to be retrieved on non-existing preference
     * @return Single value corresponding to given key
     */
    private suspend fun <T> getPref(key: Preferences.Key<T>, default: T): T {
        val preferences = context.settingsDatastore.data.first()
        return preferences[key] ?: default
    }

    /**
     * Sets preferences value to given key
     * @param key Preferences Key
     * @param value value to be set
     */
    private suspend fun <T> setPref(key: Preferences.Key<T>, value: T) {
        context.settingsDatastore.edit { settings ->
            settings[key] = value
        }
    }

    fun getTrashTypeEnabledAsFlow(trashType: Bin.TrashType) = getPrefAsFlow(booleanPreferencesKey(trashType.name), true)
    suspend fun getTrashTypeEnabled(trashType: Bin.TrashType) = getPref(booleanPreferencesKey(trashType.name), true)
    suspend fun getTrashTypeEnabled(trashTypes: List<Bin.TrashType>) = trashTypes.filter { getTrashTypeEnabled(it) }
    suspend fun setTrashType(trashType: Bin.TrashType, enabled: Boolean) = setPref(booleanPreferencesKey(trashType.name), enabled)

    fun getUseAllEnabledAsFlow() = getPrefAsFlow(allRequiredKey, true)
    suspend fun getUseAllEnabled() = getPref(allRequiredKey, true)
    suspend fun setUseAll(enabled: Boolean) = setPref(allRequiredKey, enabled)

    fun getLocationEnabledAsFlow() = getPrefAsFlow(locationEnabledKey, true)
    suspend fun getLocationEnabled() = getPref(locationEnabledKey, true)
    suspend fun setLocationEnabled(enabled: Boolean) = setPref(locationEnabledKey, enabled)

    val hasLocationPermission get() =
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    companion object {
        val allRequiredKey = booleanPreferencesKey("all_required")
        val locationEnabledKey = booleanPreferencesKey("location_enabled")
    }
}