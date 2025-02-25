package fr.isen.boussougou.isensmartcompanion

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.History
import androidx.compose.foundation.layout.padding

// Define the routes for the screens
/**
 * A sealed class that defines the different screens in the application.
 * Each screen is represented by its route (URL) and title.
 */
sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Home")
    object Events : Screen("events", "Events")
    object History : Screen("history", "History")
}

/**
 * Sets up the navigation structure of the app, including the bottom navigation bar
 * and the navigation between different screens (Home, Events, and History).
 *
 * This composable function initializes a `NavController` and a `Scaffold` to layout
 * the UI. It sets up a `NavHost` to manage the navigation between the screens, and
 * provides the bottom navigation bar for screen switching.
 */
@Composable
fun Navigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }  // Adds a bottom navigation bar
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,  // Set Home as the initial screen
            modifier = Modifier.padding(innerPadding)  // Ensures padding from Scaffold
        ) {
            // Define composable screens linked to each route
            composable(Screen.Home.route) { MainScreen() }
            composable(Screen.Events.route) { EventsScreen(navController) }
            composable(Screen.History.route) { HistoryScreen() }
        }
    }
}

/**
 * Composable function that defines the bottom navigation bar of the app.
 * It contains navigation items for switching between screens (Home, Events, and History).
 * Each item is represented by an icon and a label. When clicked, the user is navigated
 * to the corresponding screen.
 *
 * @param navController The navigation controller that manages screen transitions.
 */
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,  // Sets the background color
        contentColor = Color.White  // Sets the text/icon color
    ) {
        // Home screen navigation item
        NavigationBarItem(
            selected = false,  // Set to true when selected (could be managed dynamically)
            onClick = { navController.navigate(Screen.Home.route) },
            label = { Text(Screen.Home.title) },
            icon = { Icon(Icons.Default.Home, contentDescription = Screen.Home.title) }
        )

        // Events screen navigation item
        NavigationBarItem(
            selected = false,  // Set to true when selected (could be managed dynamically)
            onClick = { navController.navigate(Screen.Events.route) },
            label = { Text(Screen.Events.title) },
            icon = { Icon(Icons.Default.Event, contentDescription = Screen.Events.title) }
        )

        // History screen navigation item
        NavigationBarItem(
            selected = false,  // Set to true when selected (could be managed dynamically)
            onClick = { navController.navigate(Screen.History.route) },
            label = { Text(Screen.History.title) },
            icon = { Icon(Icons.Default.History, contentDescription = Screen.History.title) }
        )
    }
}