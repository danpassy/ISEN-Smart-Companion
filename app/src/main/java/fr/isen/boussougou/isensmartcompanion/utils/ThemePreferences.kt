package fr.isen.boussougou.isensmartcompanion.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("theme_preferences")

// Manages user's theme preferences (dark or light mode) using DataStore.
class ThemePreferences(context: Context) {
    private val dataStore = context.dataStore
    private val THEME_KEY = booleanPreferencesKey("dark_mode")

    // Retrieves current theme preference as a Flow<Boolean>.
    fun getTheme(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: false  // Default to false (light mode)
        }
    }

    // Saves user's theme preference (dark or light mode).
    suspend fun saveTheme(isDarkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkMode
        }
    }
}
