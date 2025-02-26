package fr.isen.boussougou.isensmartcompanion

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.Gson
import fr.isen.boussougou.isensmartcompanion.models.Event
import fr.isen.boussougou.isensmartcompanion.network.RetrofitClient
import kotlinx.coroutines.launch

@Composable
fun EventsScreen(navController: NavController) {
    var events by remember { mutableStateOf<List<Event>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    // Fetch events from API when the screen is launched.
    LaunchedEffect(key1 = true) {
        coroutineScope.launch {
            try {
                events = RetrofitClient.instance.getEvents()
            } catch (e: Exception) {
                println("Error fetching events: ${e.message}")
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally // Center-align content horizontally.
    ) {
        // Centered title at the top of the screen.
        Text(
            text = "List of Events",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.primary
        )

        // LazyColumn to display a scrollable list of events.
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Use itemsIndexed to get both the event and its index.
            itemsIndexed(events) { index, event ->
                EventItem(event, navController, index)
            }
        }
    }
}

@Composable
fun EventItem(event: Event, navController: NavController, index: Int) {
    // Alternate background colors based on index (even/odd).
    val backgroundColor =
        if (index % 2 == 0) Color(0xFF1FA055) else Color(0xFF35A966)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                val eventJson = Gson().toJson(event)
                navController.navigate("eventDetail/$eventJson")
            },
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = event.title,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = event.date,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = event.location,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}
