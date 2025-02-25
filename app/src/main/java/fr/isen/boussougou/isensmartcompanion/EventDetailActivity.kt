
package fr.isen.boussougou.isensmartcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import fr.isen.boussougou.isensmartcompanion.ui.theme.ISENSmartCompanionTheme

/**
 * EventDetailActivity is a screen that displays detailed information about an event.
 * This activity uses Jetpack Compose for the UI and applies the app's theme.
 */
class EventDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the UI content using Jetpack Compose
        setContent {
            // Apply the custom theme for consistent styling across the app
            ISENSmartCompanionTheme {
                // Surface provides a container with a background color and handles UI drawing
                Surface(
                    modifier = Modifier.fillMaxSize(), // Makes the surface fill the entire screen
                    color = MaterialTheme.colorScheme.background // Uses the background color defined in the theme
                ) {
                    EventDetailScreen() // Calls the composable function to display event details
                }
            }
        }
    }
}

/**
 * EventDetailScreen is a composable function that displays event details.
 * It centers the content both vertically and horizontally.
 */
@Composable
fun EventDetailScreen() {
    // Column arranges its children vertically
    Column(
        modifier = Modifier.fillMaxSize(), // Makes the column take up the entire available space
        verticalArrangement = Arrangement.Center, // Centers the content vertically
        horizontalAlignment = Alignment.CenterHorizontally // Centers the content horizontally
    ) {
        // Displays the event details text using a predefined style from the Material Theme
        Text(
            text = "Event Details", // Placeholder text for event information
            style = MaterialTheme.typography.headlineMedium // Applies a medium-sized headline style
        )
    }
}