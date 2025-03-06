package fr.isen.boussougou.isensmartcompanion.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_events")
data class StudentEvent(
    @PrimaryKey val eventId: String, // Unique identifier for the event
    val eventName: String,
    val eventDate: String,
    val eventLocation: String,
    // Add other relevant event details here
)
