package fr.isen.boussougou.isensmartcompanion.network

import fr.isen.boussougou.isensmartcompanion.models.Event
import retrofit2.http.GET

interface ApiService {
    @GET("events.json")
    suspend fun getEvents(): List<Event>
}
