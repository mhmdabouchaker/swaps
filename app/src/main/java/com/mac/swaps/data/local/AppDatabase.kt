package com.mac.swaps.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mac.swaps.model.Movie

@Database(entities = [Movie::class], version = 1, exportSchema = false)
//@TypeConverters(GenreConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}