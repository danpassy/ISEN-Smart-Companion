package fr.isen.boussougou.isensmartcompanion.models

// Stores user preferences for notifications related to specific events.
data class EventNotificationPreferences(
    val eventId: String,              // ID of the associated event
    val isNotificationEnabled: Boolean // Notification enabled status (true/false)
)
