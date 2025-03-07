package fr.isen.boussougou.isensmartcompanion.database

import androidx.room.*
import fr.isen.boussougou.isensmartcompanion.models.Course
import kotlinx.coroutines.flow.Flow

// DAO permettant d'accéder aux cours stockés localement dans la base Room.
@Dao
interface CourseDao {
    @Query("SELECT * FROM courses")
    fun getAllCourses(): Flow<List<Course>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: Course)

    @Delete
    suspend fun deleteCourse(course: Course)
}
