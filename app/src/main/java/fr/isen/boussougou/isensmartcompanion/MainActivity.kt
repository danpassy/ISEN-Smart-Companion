package fr.isen.boussougou.isensmartcompanion

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.vectorResource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import com.google.ai.client.generativeai.GenerativeModel
import fr.isen.boussougou.isensmartcompanion.ui.theme.ISENSmartCompanionTheme
import fr.isen.boussougou.isensmartcompanion.database.AppDatabase
import fr.isen.boussougou.isensmartcompanion.database.InteractionDao
import fr.isen.boussougou.isensmartcompanion.database.Interaction
import fr.isen.boussougou.isensmartcompanion.database.CourseDao
import fr.isen.boussougou.isensmartcompanion.database.StudentEventDao
import androidx.core.content.ContextCompat
//import fr.isen.boussougou.isensmartcompanion.components.BottomInput
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.TextFieldDefaults
import fr.isen.boussougou.isensmartcompanion.utils.NotificationHelper
//import fr.isen.boussougou.isensmartcompanion.R
import kotlinx.coroutines.launch
import androidx.compose.ui.tooling.preview.Preview
import fr.isen.boussougou.isensmartcompanion.utils.ThemePreferences

//import fr.isen.boussougou.isensmartcompanion.BuildConfig



class MainActivity : ComponentActivity() {
    /*
 Main entry point for app:
 - Initializes database instances and generative AI model.
 - Requests notification permission if required.
 - Sets up global theme preference management.
 */
    private lateinit var generativeModel: GenerativeModel
    private lateinit var database: AppDatabase
    private lateinit var interactionDao: InteractionDao
    private lateinit var courseDao: CourseDao
    private lateinit var studentEventDao: StudentEventDao

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
        courseDao = database.courseDao()
        studentEventDao = database.studentEventDao()

        generativeModel = GenerativeModel(
            modelName = "gemini-2.0-flash",
            apiKey = BuildConfig.GEMINI_API_KEY
        )
        NotificationHelper.createNotificationChannel(this)
        askNotificationPermission()

        enableEdgeToEdge()

        val themePreferences = ThemePreferences(this)

        setContent {
            val isDarkMode = themePreferences.getTheme().collectAsState(initial = false)
            ISENSmartCompanionTheme(darkTheme = isDarkMode.value) { // ✅ Correction ici (suppression des parenthèses inutiles)
                Navigation(
                    generativeModel,
                    interactionDao,
                    courseDao,
                    studentEventDao,
                    themePreferences
                )
            }
        }
    }
    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                // by them granting the POST_NOTIFICATION permission. This UI should provide the user
                // "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                // If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    generativeModel: GenerativeModel,
    interactionDao: InteractionDao,
    themePreferences: ThemePreferences
) {
    /*Home screen composable:
    - Provides input field to ask questions to AI assistant.
    - Displays AI-generated responses in scrollable area.
    - Allows switching between dark and light themes via top bar switch.
    */
    var question by remember { mutableStateOf("") }
    var response by remember { mutableStateOf("AI response will appear here.") }
    val coroutineScope = rememberCoroutineScope()
    val isDarkMode = themePreferences.getTheme().collectAsState(initial = false)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("ISEN Smart Companion")
                    }
                },
                actions = {
                    Switch(
                        checked = isDarkMode.value,
                        onCheckedChange = { darkMode ->
                            coroutineScope.launch {
                                themePreferences.saveTheme(darkMode)
                            }
                        }
                    )
                }
            )
        },
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
                            val interaction = Interaction(question = question, answer = aiResponse)
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

            Spacer(modifier = Modifier.height(16.dp))

            // Logo réduit à 60.dp pour une meilleure ergonomie
            Image(
                painter = painterResource(id = R.drawable.isen_logo),
                contentDescription = "ISEN Logo",
                modifier = Modifier.size(60.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Réponse de l'IA affichée dans une zone scrollable
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.TopCenter
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val context = LocalContext.current
    val themePreferencesPreview = ThemePreferences(context)

    ISENSmartCompanionTheme(darkTheme = false) { // ✅ Correction ici (ajout explicite du paramètre manquant)
        MainScreen(
            GenerativeModel("gemini-2.0-flash", "dummy-api-key"),
            AppDatabase.getDatabase(context).interactionDao(),
            themePreferencesPreview // ✅ Correction ici (ajout du paramètre manquant)
        )
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