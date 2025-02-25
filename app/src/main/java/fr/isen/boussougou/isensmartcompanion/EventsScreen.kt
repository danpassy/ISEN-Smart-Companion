package fr.isen.boussougou.isensmartcompanion

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

/**
 * EventsScreen is a composable function that displays the main events page.
 * It includes a title and a button that navigates to the EventDetailActivity.
 *
 * @param navController Controls navigation between screens in a Jetpack Compose app.
 */
@Composable
fun EventsScreen(navController: NavHostController) {
    // Column arranges child elements vertically
    Column(
        modifier = Modifier.fillMaxSize(), // Fills the entire screen
        verticalArrangement = Arrangement.Center, // Centers the content vertically
        horizontalAlignment = Alignment.CenterHorizontally // Centers the content horizontally
    ) {
        // Display a header text for the screen
        Text(
            text = "Events Screen", // Title text for the events screen
            style = MaterialTheme.typography.headlineMedium // Applies a headline text style
        )

        // Adds vertical spacing between the text and the button
        Spacer(modifier = Modifier.height(16.dp))

        // Button that navigates to the EventDetailActivity when clicked
        Button(
            onClick = {
                // Retrieves the current context from the NavHostController
                val context = navController.context

                // Creates an explicit intent to navigate to EventDetailActivity
                val intent = Intent(context, EventDetailActivity::class.java)

                // Starts the activity using the intent
                context.startActivity(intent)
            }
        ) {
            // The text inside the button
            Text(text = "Go to Event Details")
        }
    }
}