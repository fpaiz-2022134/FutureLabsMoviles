package plat.futurelabs.paiz.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.sessionDataStore:
        DataStore<Preferences> by preferencesDataStore(
    name = "session_preferences"
)

class SessionDataStore(
    context: Context
) {

    private val dataStore = context.applicationContext.sessionDataStore

    private companion object {
        val NAME_KEY = stringPreferencesKey("name")
    }

    val userName: Flow<String?> = dataStore.data.map { preferences ->
        preferences[NAME_KEY]
    }

    suspend fun saveName(name: String) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = name
        }
    }

    suspend fun clearName() {
        dataStore.edit { preferences ->
            preferences.remove(NAME_KEY)
        }
    }
}