package fr.isen.boussougou.isensmartcompanion

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun Navigation() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { MainScreen() }
            composable(Screen.Events.route) { EventsScreen(navController) }
            composable(Screen.History.route) { HistoryScreen() }
            composable(
                route = "eventDetail/{eventId}",
                arguments = listOf(navArgument("eventId") { type = NavType.IntType })
            ) { backStackEntry ->
                val eventId = backStackEntry.arguments?.getInt("eventId") ?: -1
                val event = fakeEvents.find { it.id == eventId }
                event?.let { EventDetailScreen(it) }
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
