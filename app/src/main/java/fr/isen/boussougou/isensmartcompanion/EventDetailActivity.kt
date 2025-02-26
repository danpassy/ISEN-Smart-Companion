package fr.isen.boussougou.isensmartcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.isen.boussougou.isensmartcompanion.models.Event
import fr.isen.boussougou.isensmartcompanion.ui.theme.ISENSmartCompanionTheme
import com.google.gson.Gson

class EventDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val eventJson = intent.getStringExtra("event")
        val event = Gson().fromJson(eventJson, Event::class.java)

        setContent {
            ISENSmartCompanionTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (event != null) {
                        EventDetailScreen(event)
                    } else {
                        Text("Event not found", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

@Composable
fun EventDetailScreen(event: Event) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally // Centrer horizontalement.
    ) {
        // Box pour afficher les détails de l'événement.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant) // Couleur adaptative pour light/dark mode.
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter // Aligner le contenu en haut.
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Date: ${event.date}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Location: ${event.location}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Category: ${event.category}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = event.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Justify // Justifier la description.
                )
            }
        }

        // Spacer flexible pour pousser le bouton vers le bas.
        Spacer(modifier = Modifier.weight(1f))

        // Bouton pour s'inscrire à l'événement.
        Button(
            onClick = { /* TODO: Handle registration */ },
            modifier = Modifier.fillMaxWidth(), // Remplir la largeur disponible.
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Register for the event", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}
