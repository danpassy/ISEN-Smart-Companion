package fr.isen.boussougou.isensmartcompanion

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun EventsScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "List of Events",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(fakeEvents) { event ->
                EventItem(event = event, navController = navController)
            }
        }
    }
}

@Composable
fun EventItem(event: Event, navController: NavController) {
    val backgroundColor = if (event.id % 2 == 0) Color(0xFF1FA055) else Color(0xFF35A966)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp) // Add vertical padding for spacing
            .clip(RoundedCornerShape(12.dp)) // Clip the card with rounded corners
            .clickable {
                navController.navigate("eventDetail/${event.id}")
            }
            .background(backgroundColor),
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
