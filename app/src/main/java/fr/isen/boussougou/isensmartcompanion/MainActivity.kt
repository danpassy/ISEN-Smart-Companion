package fr.isen.boussougou.isensmartcompanion

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
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

class MainActivity : ComponentActivity() {
    private lateinit var generativeModel: GenerativeModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        generativeModel = GenerativeModel(
            modelName = "gemini-2.0-flash",
            apiKey = "AIzaSyDxdu9bnw2hVBEHgbzED3QOW-gqmRQAUec"
        )

        enableEdgeToEdge()

        setContent {
            ISENSmartCompanionTheme {
                Navigation(generativeModel)
            }
        }
    }
}

@Composable
fun MainScreen(generativeModel: GenerativeModel) {
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
                            response = result.text ?: "No response generated"
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
        MainScreen(GenerativeModel("gemini-2.0-flash", "dummy-api-key"))
    }
}
