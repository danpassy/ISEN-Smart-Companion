package fr.isen.boussougou.isensmartcompanion

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import fr.isen.boussougou.isensmartcompanion.ui.theme.ISENSmartCompanionTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch
import fr.isen.boussougou.isensmartcompanion.database.AppDatabase
import fr.isen.boussougou.isensmartcompanion.database.InteractionDao
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build

import fr.isen.boussougou.isensmartcompanion.utils.NotificationHelper


class MainActivity : ComponentActivity() {
    private lateinit var generativeModel: GenerativeModel
    private lateinit var database: AppDatabase
    private lateinit var interactionDao: InteractionDao

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your app.
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = AppDatabase.getDatabase(this)
        interactionDao = database.interactionDao()

        generativeModel = GenerativeModel(
            modelName = "gemini-2.0-flash",
            apiKey = BuildConfig.GEMINI_API_KEY
        )

        NotificationHelper.createNotificationChannel(this)
        askNotificationPermission()

        enableEdgeToEdge()

        setContent {
            ISENSmartCompanionTheme {
                Navigation(generativeModel, interactionDao)
            }
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

@Composable
fun MainScreen(generativeModel: GenerativeModel, interactionDao: InteractionDao) {
    var question by remember { mutableStateOf("") }
    var response by remember { mutableStateOf("AI response will appear here.") }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            BottomInput(
                question = question,
                onQuestionChange = { question = it },
                onSendClick = {
                    coroutineScope.launch {
                        try {
                            val result = generativeModel.generateContent(question)
                            val aiResponse = result.text ?: "No response generated"
                            response = aiResponse

                            val interaction = fr.isen.boussougou.isensmartcompanion.database.Interaction(question = question, answer = aiResponse)
                            interactionDao.insertInteraction(interaction)

                        } catch (e: Exception) {
                            response = "Error: ${e.message}"
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display the app title
            Text(
                text = "ISEN Smart Companion",
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            // Display the logo of the app
            Image(
                painter = painterResource(id = R.drawable.isen_logo),
                contentDescription = "ISEN Logo",
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Make the AI response scrollable
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Take up available vertical space
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()), // Enable vertical scrolling
                contentAlignment = Alignment.TopCenter // Align text to the top of the box
            ) {
                Text(
                    text = response,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun BottomInput(
    question: String,
    onQuestionChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = question,
            onValueChange = onQuestionChange,
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(24.dp)),
            placeholder = { Text("Ask your question...") },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(24.dp)
        )

        IconButton(
            onClick = {
                Toast.makeText(context, "Question Submitted", Toast.LENGTH_SHORT).show()
                onSendClick()
            },
            modifier = Modifier.background(color = Color(0xFF03DAC5),
                shape = RoundedCornerShape(24.dp))
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_arrow_forward_24),
                contentDescription = "Send",
                tint = Color.Black
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ISENSmartCompanionTheme {
        MainScreen(GenerativeModel("gemini-2.0-flash", "dummy-api-key"), AppDatabase.getDatabase(LocalContext.current).interactionDao())
    }
}
