package fr.isen.boussougou.isensmartcompanion.models

import androidx.room.Entity
import androidx.room.PrimaryKey

// Represents a course entity stored in the local Room database.
@Entity(tableName = "courses")
data class Course(
    @PrimaryKey val courseId: String, // Unique identifier for the course
    val courseName: String,           // Name of the course
    val teacherName: String,          // Name of the teacher
    val time: String,                 // Time of the course
    val location: String              // Location of the course
)
