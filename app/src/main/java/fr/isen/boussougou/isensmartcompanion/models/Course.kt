package fr.isen.boussougou.isensmartcompanion.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class Course(
    @PrimaryKey val courseId: String, // Unique identifier for the course
    val courseName: String,
    val teacherName: String,
    val time: String,
    val location: String,
)
