package fr.isen.boussougou.isensmartcompanion

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.isen.boussougou.isensmartcompanion.database.CourseDao
import fr.isen.boussougou.isensmartcompanion.models.Course
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.UUID
import androidx.compose.ui.platform.LocalContext
import fr.isen.boussougou.isensmartcompanion.utils.ThemePreferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaScreen(courseDao: CourseDao, themePreferences: ThemePreferences) {
    /*
   Displays user's agenda screen:
   - Loads courses from JSON into database on launch.
   - Shows list of courses if available.
   - Allows switching between dark and light themes.
   */
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val courses = courseDao.getAllCourses().collectAsState(initial = emptyList())
    val isDarkMode = themePreferences.getTheme().collectAsState(initial = false)

    LaunchedEffect(key1 = true) {
        coroutineScope.launch {
            loadCoursesFromJson(context, courseDao)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Agenda",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
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
        Column(modifier = Modifier.padding(innerPadding)) {
            if (courses.value.isEmpty()) {
                Text(
                    text = "No courses available. Loading...",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn {
                    items(courses.value) { course ->
                        CourseItem(course)
                    }
                }
            }
        }
    }
}

@Composable
fun CourseItem(course: Course) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = course.courseName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Teacher: ${course.teacherName}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = "Time: ${course.time}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = "Location: ${course.location}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}
suspend fun loadCoursesFromJson(context: Context, courseDao: CourseDao) {
    try {
        val inputStream = context.assets.open("planning.JSON")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        println("JSON Loaded Successfully: $jsonString") // Log pour vérifier le contenu
        val jsonObject = JSONObject(jsonString)
        val weeksArray = jsonObject.getJSONArray("weeks")

        for (i in 0 until weeksArray.length()) {
            val weekObject = weeksArray.getJSONObject(i)
            val scheduleArray = weekObject.getJSONArray("schedule")

            for (j in 0 until scheduleArray.length()) {
                val scheduleObject = scheduleArray.getJSONObject(j)

                // Extraction des données
                val courseName = scheduleObject.getString("subject")
                val teacherName = scheduleObject.optString("teacher", "Unknown")
                val time = scheduleObject.optString("time", "Unknown")
                val location = scheduleObject.optString("location", "Unknown")

                // Création d'un objet Course
                val course = Course(
                    courseId = UUID.randomUUID().toString(),
                    courseName = courseName,
                    teacherName = teacherName,
                    time = time,
                    location = location
                )

                // Insertion dans la base de données
                courseDao.insertCourse(course)
            }
        }

        println("Courses loaded successfully")
    } catch (e: Exception) {
        println("Error loading courses: ${e.message}")
    }
}

