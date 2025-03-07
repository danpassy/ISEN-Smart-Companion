package fr.isen.boussougou.isensmartcompanion

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.ai.client.generativeai.GenerativeModel
import com.google.gson.Gson
import fr.isen.boussougou.isensmartcompanion.models.Event
import fr.isen.boussougou.isensmartcompanion.database.InteractionDao
import fr.isen.boussougou.isensmartcompanion.utils.EventNotificationPreferencesManager
import fr.isen.boussougou.isensmartcompanion.database.CourseDao
import fr.isen.boussougou.isensmartcompanion.database.StudentEventDao
import fr.isen.boussougou.isensmartcompanion.utils.ThemePreferences

sealed class Screen(val route: String, val icon: ImageVector, val title: String) {
    object Home : Screen("home", Icons.Default.Home, "Home")
    object Events : Screen("events", Icons.Default.Event, "Events")
    object History : Screen("history", Icons.Default.History, "History")
    object Agenda : Screen("agenda", Icons.Filled.CalendarMonth, "Agenda")
}

@Composable
fun Navigation(
    generativeModel: GenerativeModel,
    interactionDao: InteractionDao,
    courseDao: CourseDao,
    studentEventDao: StudentEventDao,
    themePreferences: ThemePreferences
) {
    /*
Manages navigation between different screens using Jetpack Compose Navigation:
- Defines navigation routes for Home, Events, History and Agenda screens clearly.
- Passes necessary dependencies (database DAOs and theme preferences) to each screen composable explicitly.
*/
    val navController = rememberNavController()
    val context = LocalContext.current
    val notificationPrefsManager = remember { EventNotificationPreferencesManager(context) }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { MainScreen(generativeModel, interactionDao, themePreferences) }
            composable(Screen.Events.route) { EventsScreen(navController, themePreferences) }
            composable(Screen.History.route) { HistoryScreen(interactionDao, navController, themePreferences) }
            composable(Screen.Agenda.route) { AgendaScreen(courseDao, themePreferences) }
            composable(
                route = "eventDetail/{eventJson}",
                arguments = listOf(navArgument("eventJson") { type = NavType.StringType })
            ) { backStackEntry ->
                val eventJson = backStackEntry.arguments?.getString("eventJson")
                val event = Gson().fromJson(eventJson, Event::class.java)
                event?.let { EventDetailScreen(it, notificationPrefsManager, studentEventDao) }
            }
            composable(
                route = "interactionDetail/{interactionId}",
                arguments = listOf(navArgument("interactionId") { type = NavType.IntType })
            ) { backStackEntry ->
                val interactionId = backStackEntry.arguments?.getInt("interactionId") ?: -1
                InteractionDetailScreen(interactionId, interactionDao, navController)
            }
            composable(Screen.Agenda.route) { AgendaScreen(courseDao = courseDao,themePreferences) }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    /*
 Bottom navigation bar component:
 - Provides easy navigation between main sections (Home, Events, History & Agenda).
 - Highlights currently selected item clearly for better user experience.
 */

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Screen.Home.icon, contentDescription = null) },
            label = { Text(Screen.Home.title) },
            selected = navController.currentDestination?.route == Screen.Home.route,
            onClick = { navController.navigate(Screen.Home.route) }
        )
        NavigationBarItem(
            icon = { Icon(Screen.Events.icon, contentDescription = null) },
            label = { Text(Screen.Events.title) },
            selected = navController.currentDestination?.route == Screen.Events.route,
            onClick = { navController.navigate(Screen.Events.route) }
        )
        NavigationBarItem(
            icon = { Icon(Screen.History.icon, contentDescription = null) },
            label = { Text(Screen.History.title) },
            selected = navController.currentDestination?.route == Screen.History.route,
            onClick = { navController.navigate(Screen.History.route) }
        )
        NavigationBarItem(
            icon = { Icon(Screen.Agenda.icon, contentDescription = null) },
            label = { Text(Screen.Agenda.title) },
            selected = navController.currentDestination?.route == Screen.Agenda.route,
            onClick = { navController.navigate(Screen.Agenda.route) }
        )
    }
}
