package fr.isen.boussougou.isensmartcompanion

import java.io.Serializable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.History


data class Event(
    val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val category: String
) : Serializable

sealed class Screen(val route: String, val icon: ImageVector, val title: String) {
    object Home : Screen("home", Icons.Default.Home, "Home")
    object Events : Screen("events", Icons.Default.Event, "Events")
    object History : Screen("history", Icons.Default.History, "History")
}

// Liste d'événements factices pour le test
val fakeEvents = listOf(
    Event(1, "BDE Evening", "Une soirée amusante organisée par le BDE.", "2025-03-01", "Campus ISEN", "Social"),
    Event(2, "Gala Night", "Soirée de gala annuelle avec dîner et musique.", "2025-03-15", "Grand Hôtel", "Formel"),
    Event(3, "Cohesion Day", "Activités de team-building pour les étudiants.", "2025-02-28", "Terrain de sport ISEN", "Sports"),
    Event(4, "Tech Conference", "Conférence sur les dernières technologies.", "2025-04-10", "Auditorium ISEN", "Education"),
    Event(5, "Career Fair", "Salon de l'emploi pour les étudiants.", "2025-05-05", "Hall principal ISEN", "Carrière"),
    Event(6, "Hackathon", "48 heures de programmation intensive.", "2025-06-20", "Laboratoires ISEN", "Technologie"),
    Event(7, "Alumni Meetup", "Rencontre avec les anciens élèves.", "2025-07-08", "Jardin ISEN", "Networking"),
    Event(8, "Sports Tournament", "Tournoi sportif inter-écoles.", "2025-09-15", "Complexe sportif ISEN", "Sports")
)