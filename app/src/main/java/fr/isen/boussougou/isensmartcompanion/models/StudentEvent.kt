package fr.isen.boussougou.isensmartcompanion.models

import androidx.room.Entity
import androidx.room.PrimaryKey

// Represents an event added by a student to their personal agenda in local Room database.
@Entity(tableName = "student_events")
data class StudentEvent(
    @PrimaryKey val eventId: String,  // Unique identifier for the student-added event
    val eventName: String,            // Name of the event added by student
    val eventDate: String,            // Date of the student-added event
    val eventLocation: String         // Location of the student-added event
)
