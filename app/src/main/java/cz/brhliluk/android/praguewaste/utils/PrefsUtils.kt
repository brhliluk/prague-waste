package cz.brhliluk.android.praguewaste.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import cz.brhliluk.android.praguewaste.model.Bin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

const val LOCATION_PREFS_KEY = "use_location"
val USE_LOCATION = booleanPreferencesKey(LOCATION_PREFS_KEY)

val Context.useLocationEnabled: Flow<Boolean>
    get() = this.dataStore.data.map { preferences -> preferences[USE_LOCATION] ?: true }

suspend fun Context.flipLocationEnabled() {
    this.dataStore.edit { settings ->
        val currentCounterValue = settings[USE_LOCATION] ?: true
        settings[USE_LOCATION] = !currentCounterValue
    }
}

suspend fun Context.setLocationEnabled(enabled: Boolean) =
    this.dataStore.edit { settings -> settings[USE_LOCATION] = enabled }

fun Bin.TrashType.isEnabled(context: Context): Flow<Boolean> =
    context.dataStore.data.map { preferences ->
        preferences[booleanPreferencesKey(this.name)] ?: true
    }

suspend fun Bin.TrashType.setEnabled(context: Context, enabled: Boolean) {
    context.dataStore.edit { settings ->
        val currentCounterValue = settings[booleanPreferencesKey(this.name)] ?: true
        settings[booleanPreferencesKey(this.name)] = !currentCounterValue
    }
}