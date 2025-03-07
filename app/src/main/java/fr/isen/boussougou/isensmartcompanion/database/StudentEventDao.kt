package fr.isen.boussougou.isensmartcompanion.database

import androidx.room.*
import fr.isen.boussougou.isensmartcompanion.models.StudentEvent
import kotlinx.coroutines.flow.Flow

// DAO permettant d'accéder aux événements ajoutés par les étudiants dans la base locale.
@Dao
interface StudentEventDao {
    @Query("SELECT * FROM student_events")
    fun getAllStudentEvents(): Flow<List<StudentEvent>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudentEvent(studentEvent: StudentEvent)

    @Delete
    suspend fun deleteStudentEvent(studentEvent: StudentEvent)
}
