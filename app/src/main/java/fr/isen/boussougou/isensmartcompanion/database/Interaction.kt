package fr.isen.boussougou.isensmartcompanion.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

// Représente une interaction entre l'utilisateur et l'assistant IA stockée dans la base locale.

@Entity(tableName = "interactions")
data class Interaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val question: String,
    val answer: String,
    val timestamp: Date = Date()
)
