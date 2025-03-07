package fr.isen.boussougou.isensmartcompanion.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.isen.boussougou.isensmartcompanion.models.Course
//import fr.isen.boussougou.isensmartcompanion.models.Interaction
import fr.isen.boussougou.isensmartcompanion.models.StudentEvent

// Définition de la base de données Room avec ses entités et convertisseurs.
@Database(entities = [Interaction::class, Course::class, StudentEvent::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    // Fournit l'accès au DAO pour les interactions utilisateur-IA.
    abstract fun interactionDao(): InteractionDao

    // Fournit l'accès au DAO pour la gestion des cours.
    abstract fun courseDao(): CourseDao

    // Fournit l'accès au DAO pour les événements ajoutés par les étudiants.
    abstract fun studentEventDao(): StudentEventDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        // Crée ou retourne l'instance unique de la base de données.
        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
