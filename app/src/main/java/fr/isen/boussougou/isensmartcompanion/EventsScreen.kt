package fr.isen.boussougou.isensmartcompanion

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
//import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.Gson
import fr.isen.boussougou.isensmartcompanion.models.Event
import fr.isen.boussougou.isensmartcompanion.network.RetrofitClient
import kotlinx.coroutines.launch
import fr.isen.boussougou.isensmartcompanion.utils.ThemePreferences


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(navController: NavController, themePreferences: ThemePreferences) {


    var events by remember { mutableStateOf<List<Event>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()
    val isDarkMode = themePreferences.getTheme().collectAsState(initial = false)


    LaunchedEffect(key1 = true) {
        coroutineScope.launch {
            try {
                events = RetrofitClient.instance.getEvents()
            } catch (e: Exception) {
                println("Error fetching events: ${e.message}")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Events Screen") },
                actions = {
                    Switch(
                        checked = isDarkMode.value,
                        onCheckedChange = { darkMode ->
                            coroutineScope.launch { themePreferences.saveTheme(darkMode) }
                        }
                    )
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(events) { event ->
                EventItem(event, navController)
            }
        }
    }
}

@Composable
fun EventItem(event: Event, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                val eventJson = Gson().toJson(event)
                navController.navigate("eventDetail/$eventJson")
            },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = event.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = event.date,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = event.location,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        }
    }
}
