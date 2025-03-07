package fr.isen.boussougou.isensmartcompanion.database

import androidx.room.TypeConverter
import java.util.Date

// Convertisseurs permettant à Room de stocker des types complexes comme Date dans la base locale.
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
