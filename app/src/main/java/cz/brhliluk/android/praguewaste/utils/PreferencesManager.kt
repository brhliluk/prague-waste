package cz.brhliluk.android.praguewaste.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import cz.brhliluk.android.praguewaste.model.Bin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent

class PreferencesManager(private val context: Context) : KoinComponent {

    fun getTrashTypeEnabledAsFlow(trashType: Bin.TrashType): Flow<Boolean> =
        context.settingsDatastore.data.map { preferences ->
            preferences[booleanPreferencesKey(trashType.name)] ?: true
        }

    suspend fun getTrashTypeEnabled(trashType: Bin.TrashType): Boolean {
        val preferences = context.settingsDatastore.data.first()
        return preferences[booleanPreferencesKey(trashType.name)] ?: true
    }

    suspend fun getTrashTypeEnabled(trashTypes: List<Bin.TrashType>) =
        trashTypes.filter { getTrashTypeEnabled(it) }

    suspend fun setTrashType(trashType: Bin.TrashType, enabled: Boolean) {
        context.settingsDatastore.edit { settings ->
            settings[booleanPreferencesKey(trashType.name)] = enabled
        }
    }

    suspend fun getUseAllEnabled(): Boolean {
        val preferences = context.settingsDatastore.data.first()
        return preferences[allRequiredKey] ?: false
    }

    suspend fun setUseAll(enabled: Boolean) {
        context.settingsDatastore.edit { settings ->
            settings[allRequiredKey] = enabled
        }
    }

    fun getUseAllEnabledAsFlow(): Flow<Boolean> =
        context.settingsDatastore.data.map { preferences ->
            preferences[allRequiredKey] ?: true
        }

    companion object {
        private val Context.settingsDatastore by preferencesDataStore(name = "settings")
        val allRequiredKey = booleanPreferencesKey("all_required")
    }
}