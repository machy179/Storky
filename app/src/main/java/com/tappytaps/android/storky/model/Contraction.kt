package com.tappytaps.android.storky.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Calendar

@Entity(tableName = "contractions_tbl")
data class Contraction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "length_of_contraction")
    val lengthOfContraction: Int,

    @ColumnInfo(name = "time_between_contraction")
    val timeBetweenContractions: Int,

    @ColumnInfo(name = "contraction_time")
    @TypeConverters(ContractionTypeConverter::class)
    val contractionTime: Calendar,

    @ColumnInfo(name = "in_history")
    val inHistory: Boolean = false, //if this Contraction is visible in HomeScreen, it is false, if it is visible just in History, it is true

    @ColumnInfo(name = "in_set") //for set up, in which set of history it is - when the user saves the current list to history
    val in_set: Int = 0,
)