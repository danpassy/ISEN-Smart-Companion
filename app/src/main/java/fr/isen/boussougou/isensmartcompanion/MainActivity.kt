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


/**
 * MainActivity is the entry point of the app.
 * This class sets up the main screen using Jetpack Compose
 * and applies the theme and layout.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enables edge-to-edge drawing for better UI experience
        enableEdgeToEdge()

        // Sets the content of the app with a custom theme
        setContent {
            ISENSmartCompanionTheme {
                Navigation()
            }
        }
    }
}

/**
 * MainScreen is the primary composable that builds the UI.
 * It displays the app title, logo, response area, and input field.
 */
@Composable
fun MainScreen() {
    // State variable to store the user's input question
    var question by remember { mutableStateOf("") }

    // State variable to display a fake response from the AI
    var response by remember { mutableStateOf("AI response will appear here.") }

    // Scaffold provides a standard layout structure with a bottom bar
    Scaffold(
        bottomBar = {
            BottomInput(
                question = question,
                onQuestionChange = { question = it },
                onSendClick = {
                    // Updates the response based on the user’s input
                    response = "You asked: $question"
                }
            )
        }
    ) { innerPadding ->
        // Main content of the screen
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
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Display the logo of the app
            Image(
                painter = painterResource(id = R.drawable.isen_logo),
                contentDescription = "ISEN Logo",
                modifier = Modifier.size(100.dp) // Customize logo size if needed
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Display the AI response in the center
            Text(
                text = response,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * BottomInput displays a text input field and a send button.
 *
 * @param question The text input entered by the user.
 * @param onQuestionChange Callback triggered when the text input changes.
 * @param onSendClick Callback triggered when the send button is clicked.
 */
@Composable
fun BottomInput(
    question: String,
    onQuestionChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    // Get the current context for displaying the Toast.
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Text input field for the user to ask questions
        TextField(
            value = question,
            onValueChange = onQuestionChange,
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(24.dp)), // Rounded corners
            placeholder = { Text("Ask your question...") },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(24.dp)
        )

        // Send button with an arrow icon
        IconButton(
            onClick = {// Display the Toast and trigger the onSendClick callback.
            Toast.makeText(context, "Question Submitted", Toast.LENGTH_SHORT).show()
                    onSendClick()
            },
            modifier = Modifier.background(color = Color(0xFF03DAC5),
                shape = RoundedCornerShape(24.dp))
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_arrow_forward_24),
                contentDescription = "Send",
                tint = Color.Black // Changement de la couleur de l'icône pour qu'elle soit visible sur le fond teal
            )
        }
    }
}

/**
 * DefaultPreview provides a preview of the MainScreen in Android Studio.
 * This is useful for seeing UI changes without running the app.
 */
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ISENSmartCompanionTheme {
        MainScreen()
    }
}
