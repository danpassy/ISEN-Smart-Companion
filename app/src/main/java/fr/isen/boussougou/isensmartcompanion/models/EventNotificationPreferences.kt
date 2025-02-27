package fr.isen.boussougou.isensmartcompanion.models

data class EventNotificationPreferences(
    val eventId: String,
    val isNotificationEnabled: Boolean
)
