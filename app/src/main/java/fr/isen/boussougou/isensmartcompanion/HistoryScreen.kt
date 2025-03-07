package fr.isen.boussougou.isensmartcompanion

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.background
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.isen.boussougou.isensmartcompanion.database.Interaction
import fr.isen.boussougou.isensmartcompanion.database.InteractionDao
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import fr.isen.boussougou.isensmartcompanion.utils.ThemePreferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    interactionDao: InteractionDao,
    navController: NavController,
    themePreferences: ThemePreferences
) {
    val coroutineScope = rememberCoroutineScope()
    val interactions by interactionDao.getAllInteractions().collectAsState(initial = emptyList())
    var selectedItems by remember { mutableStateOf(setOf<Int>()) }
    val isDarkMode = themePreferences.getTheme().collectAsState(initial = false)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Interaction History") },
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
        floatingActionButton = {
            if (selectedItems.isNotEmpty()) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            interactionDao.deleteInteractions(selectedItems.toList())
                            selectedItems = emptySet()
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Text("Delete")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (interactions.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No history available",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(interactions) { interaction ->
                        InteractionItem(
                            interaction,
                            navController,
                            isSelected = selectedItems.contains(interaction.id),
                            onSelectionChanged = { isSelected ->
                                selectedItems =
                                    if (isSelected) selectedItems + interaction.id else selectedItems - interaction.id
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InteractionItem(
    interaction: Interaction,
    navController: NavController,
    isSelected: Boolean,
    onSelectionChanged: (Boolean) -> Unit
) {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val question = interaction.question
    val answerPreview = interaction.answer.lines().take(3).joinToString("\n")
    val date = dateFormatter.format(interaction.timestamp)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { navController.navigate("interactionDetail/${interaction.id}") },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onSelectionChanged(it) }
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = question,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = answerPreview,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Date: $date",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InteractionDetailScreen(interactionId: Int, interactionDao: InteractionDao, navController: NavController) {
    val interaction = interactionDao.getInteractionById(interactionId).collectAsState(initial = null)
    val interactionValue = interaction.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Interaction Detail") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                )
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
            Text(
                text = "ISEN Smart Companion",
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (interactionValue != null) {
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
                    Column {
                        Text(
                            text = "Question: ${interactionValue.question}",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Answer: ${interactionValue.answer}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            } else {
                Text("Interaction not found")
            }
        }
    }
}
