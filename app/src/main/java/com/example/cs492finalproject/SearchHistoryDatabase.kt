package com.example.cs492finalproject

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Database for SearchHistory
@Database(entities = [SearchHistory::class], version = 1, exportSchema = false)
abstract class SearchHistoryDatabase : RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao

    companion object {
        @Volatile private var instance: SearchHistoryDatabase? = null

        fun getInstance(context: Context): SearchHistoryDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                SearchHistoryDatabase::class.java,
                "search-history-db"
            ).build()
    }
}