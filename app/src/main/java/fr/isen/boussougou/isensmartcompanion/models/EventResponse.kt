package fr.isen.boussougou.isensmartcompanion.models

// Represents a response from the web service containing a list of events.
data class EventResponse(
    val events: List<Event>  // List of fetched events from API call
)
