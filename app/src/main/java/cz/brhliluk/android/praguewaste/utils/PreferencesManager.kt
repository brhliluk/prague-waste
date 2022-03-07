package cz.brhliluk.android.praguewaste.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import cz.brhliluk.android.praguewaste.model.Bin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import org.koin.core.component.KoinComponent

class PreferencesManager(private val context: Context) : KoinComponent {

    fun getEnabled(trashType: Bin.TrashType): Flow<Boolean> =
        context.settingsDatastore.data.map { preferences ->
            preferences[booleanPreferencesKey(trashType.name)] ?: true
        }

//    suspend fun getEnabled(trashType: Bin.TrashType): Boolean {
//        val preferences = context.settingsDatastore.data.first()
//        return preferences[booleanPreferencesKey(trashType.name)] ?: true
//    }

    // Some .single crashes app
    suspend fun getEnabled(trashTypes: List<Bin.TrashType>) = trashTypes.filter { getEnabled(it).single() }

    suspend fun setEnabled(trashType: Bin.TrashType, enabled: Boolean) {
        context.settingsDatastore.edit { settings ->
            val currentCounterValue = settings[booleanPreferencesKey(trashType.name)] ?: true
            settings[booleanPreferencesKey(trashType.name)] = !currentCounterValue
        }
    }

    companion object {
        private val Context.settingsDatastore by preferencesDataStore(name = "settings")
    }
}