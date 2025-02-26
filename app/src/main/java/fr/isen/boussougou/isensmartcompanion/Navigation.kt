package fr.isen.boussougou.isensmartcompanion

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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

sealed class Screen(val route: String, val icon: ImageVector, val title: String) {
    object Home : Screen("home", Icons.Default.Home, "Home")
    object Events : Screen("events", Icons.Default.Event, "Events")
    object History : Screen("history", Icons.Default.History, "History")
}

@Composable
fun Navigation(generativeModel: GenerativeModel, interactionDao: InteractionDao) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { MainScreen(generativeModel, interactionDao) }
            composable(Screen.Events.route) { EventsScreen(navController) }
            composable(Screen.History.route) { HistoryScreen(interactionDao, navController) }
            composable(
                route = "eventDetail/{eventJson}",
                arguments = listOf(navArgument("eventJson") { type = NavType.StringType })
            ) { backStackEntry ->
                val eventJson = backStackEntry.arguments?.getString("eventJson")
                val event = Gson().fromJson(eventJson, Event::class.java)
                event?.let { EventDetailScreen(it) }
            }
            composable(
                route = "interactionDetail/{interactionId}",
                arguments = listOf(navArgument("interactionId") { type = NavType.IntType })
            ) { backStackEntry ->
                val interactionId = backStackEntry.arguments?.getInt("interactionId") ?: -1
                InteractionDetailScreen(interactionId, interactionDao, navController)
            }

        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
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
    }
}
