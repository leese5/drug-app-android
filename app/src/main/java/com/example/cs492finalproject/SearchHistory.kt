package com.example.cs492finalproject

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history")
data class SearchHistory(
    val id: Long = System.currentTimeMillis(),
    @PrimaryKey val query: String
)