package fr.isen.boussougou.isensmartcompanion.models

import java.io.Serializable

// Represents an event with details such as title, description, date, etc.
data class Event(
    val id: String,            // Unique identifier for the event
    val title: String,         // Title of the event
    val description: String,   // Description of the event
    val date: String,          // Date of the event
    val location: String,      // Location where the event takes place
    val category: String       // Category of the event (e.g., BDE, Gala)
) : Serializable              // Serializable to pass between activities/screens
