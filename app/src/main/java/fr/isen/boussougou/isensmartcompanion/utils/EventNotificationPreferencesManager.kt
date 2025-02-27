package fr.isen.boussougou.isensmartcompanion.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "event_notification_prefs")

class EventNotificationPreferencesManager(private val context: Context) {

    private fun getEventPreferenceKey(eventId: String): Preferences.Key<Boolean> {
        return booleanPreferencesKey("event_notification_$eventId")
    }

    suspend fun setEventNotificationPreference(eventId: String, isEnabled: Boolean) {
        val eventPreferenceKey = getEventPreferenceKey(eventId)
        context.dataStore.edit { preferences ->
            preferences[eventPreferenceKey] = isEnabled
        }
    }

    fun getEventNotificationPreference(eventId: String): Flow<Boolean> {
        val eventPreferenceKey = getEventPreferenceKey(eventId)
        return context.dataStore.data.map { preferences ->
            preferences[eventPreferenceKey] ?: false
        }
    }
}
