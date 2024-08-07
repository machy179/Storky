package com.tappytaps.android.storky.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tappytaps.android.storky.model.Contraction
import com.tappytaps.android.storky.model.ContractionTypeConverter

@Database(entities = [Contraction::class], version = 5, exportSchema = false)
@TypeConverters(ContractionTypeConverter::class)
abstract class StorkyDatabase : RoomDatabase() {
    abstract fun storkyDao(): StorkyDatabaseDao
}