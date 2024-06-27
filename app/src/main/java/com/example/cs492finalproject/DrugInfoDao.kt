package com.example.cs492finalproject

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface DrugInfoDao {
    @Insert
    suspend fun insert(drug: DrugInfo)

    @Delete
    suspend fun delete(drug: DrugInfo)

    @Query("SELECT * FROM DrugInfo")
    fun getAllRepos() : Flow<List<DrugInfo>>

    @Query("SELECT * FROM DrugInfo WHERE title = :name LIMIT 1")
    fun getRepoByName(name: String) : Flow<DrugInfo?>

}