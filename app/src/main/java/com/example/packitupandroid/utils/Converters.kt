package com.example.packitupandroid.utils

import androidx.room.TypeConverter
import java.util.Date


/**
 * Converters for Room database to handle custom data types.
 */
class Converters {

    /**
     * Converts a Long timestamp to a Date object.
     *
     * @param value The Long timestamp to convert.
     * @return A Date object corresponding to the timestamp, or null if the timestamp is null.
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    /**
     * Converts a Date object to a Long timestamp.
     *
     * @param date The Date object to convert.
     * @return A Long timestamp corresponding to the Date, or 0L if the Date is null.
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long {
        return date?.time?.toLong() ?: 0L // Set default to 0 if date is null
    }
}
