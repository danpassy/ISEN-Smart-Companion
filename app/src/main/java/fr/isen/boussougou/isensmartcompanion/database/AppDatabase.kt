package fr.isen.boussougou.isensmartcompanion.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.isen.boussougou.isensmartcompanion.models.Course
//import fr.isen.boussougou.isensmartcompanion.models.Interaction
import fr.isen.boussougou.isensmartcompanion.models.StudentEvent

@Database(entities = [Interaction::class, Course::class, StudentEvent::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun interactionDao(): InteractionDao
    abstract fun courseDao(): CourseDao
    abstract fun studentEventDao(): StudentEventDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

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
