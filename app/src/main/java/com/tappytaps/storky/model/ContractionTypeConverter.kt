package com.tappytaps.storky.model

import android.annotation.SuppressLint
import androidx.room.TypeConverter
import java.time.Instant
import java.util.Calendar

class ContractionTypeConverter {

    @TypeConverter
    fun fromCalendar(calendar: Calendar?): Long? {
        return calendar?.timeInMillis
    }

    @TypeConverter
    fun toCalendar(value: Long?): Calendar? {
        return if (value == null) null else Calendar.getInstance().apply {
            timeInMillis = value
        }
    }
}