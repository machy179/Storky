package com.tappytaps.storky.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tappytaps.storky.model.Contraction
import com.tappytaps.storky.model.ContractionTypeConverter

@Database(entities = [Contraction::class], version = 4, exportSchema = false)
@TypeConverters(ContractionTypeConverter::class)
abstract class StorkyDatabase : RoomDatabase() {
    abstract fun storkyDao(): StorkyDatabaseDao
}