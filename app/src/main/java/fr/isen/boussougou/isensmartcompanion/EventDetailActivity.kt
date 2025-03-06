package fr.isen.boussougou.isensmartcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.isen.boussougou.isensmartcompanion.models.Event
import fr.isen.boussougou.isensmartcompanion.ui.theme.ISENSmartCompanionTheme
import com.google.gson.Gson
import androidx.compose.ui.platform.LocalContext
import fr.isen.boussougou.isensmartcompanion.utils.EventNotificationPreferencesManager
import fr.isen.boussougou.isensmartcompanion.utils.NotificationHelper
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import kotlinx.coroutines.launch
import fr.isen.boussougou.isensmartcompanion.models.StudentEvent
import fr.isen.boussougou.isensmartcompanion.database.StudentEventDao
import fr.isen.boussougou.isensmartcompanion.database.AppDatabase
class EventDetailActivity : ComponentActivity() {
    private lateinit var eventNotificationPreferencesManager: EventNotificationPreferencesManager
    private lateinit var studentEventDao: StudentEventDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        eventNotificationPreferencesManager = EventNotificationPreferencesManager(this)
        studentEventDao = AppDatabase.getDatabase(this).studentEventDao()

        val eventJson = intent.getStringExtra("event")
        val event = Gson().fromJson(eventJson, Event::class.java)

        setContent {
            ISENSmartCompanionTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (event != null) {
                        EventDetailScreen(event, eventNotificationPreferencesManager, studentEventDao)
                    } else {
                        Text("Event not found", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

@Composable
fun EventDetailScreen(
    event: Event,
    notificationPrefsManager: EventNotificationPreferencesManager,
    studentEventDao: StudentEventDao
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val isNotificationEnabled = notificationPrefsManager.getEventNotificationPreference(event.id).collectAsState(initial = false)
    var isEventAdded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
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
                    textAlign = TextAlign.Justify
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    // Ajouter l'événement à l'agenda étudiant dans Room Database.
                    val studentEvent = StudentEvent(
                        eventId = event.id,
                        eventName = event.title,
                        eventDate = event.date,
                        eventLocation = event.location
                    )

                    coroutineScope.launch {
                        studentEventDao.insertStudentEvent(studentEvent)
                        isEventAdded = true // Mettre à jour l'état pour afficher le message de confirmation.
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Add to Agenda", color = MaterialTheme.colorScheme.onPrimary)
            }

            IconToggleButton(
                checked = isNotificationEnabled.value,
                onCheckedChange = { enabled ->
                    coroutineScope.launch {
                        notificationPrefsManager.setEventNotificationPreference(event.id, enabled)
                        if (enabled) {
                            NotificationHelper.scheduleNotification(context, event.title, event.description, 10)
                        } else {
                            NotificationHelper.cancelNotification(context, event.id.hashCode())
                        }
                    }
                }
            ) {
                Icon(
                    imageVector =
                    if (isNotificationEnabled.value) Icons.Filled.Notifications else Icons.Filled.NotificationsOff,
                    contentDescription =
                    if (isNotificationEnabled.value) "Disable Notifications" else "Enable Notifications"
                )
            }
        }

        if (isEventAdded) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Event added to agenda!", color = MaterialTheme.colorScheme.primary)
        }
    }
}
