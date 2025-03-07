package fr.isen.boussougou.isensmartcompanion.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow


// DAO permettant d'accéder aux interactions utilisateur-IA stockées localement.
@Dao
interface InteractionDao {
    @Query("SELECT * FROM interactions ORDER BY timestamp DESC")
    fun getAllInteractions(): Flow<List<Interaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInteraction(interaction: Interaction)

    @Delete
    suspend fun deleteInteraction(interaction: Interaction)


    @Query("DELETE FROM interactions WHERE id IN (:ids)")
    suspend fun deleteInteractions(ids: List<Int>)

    @Query("DELETE FROM interactions")
    suspend fun deleteAllInteractions()

    // Add this method to retrieve an interaction by its ID
    @Query("SELECT * FROM interactions WHERE id = :id")
    fun getInteractionById(id: Int): Flow<Interaction?>
}
