package com.example.cs492finalproject

import androidx.lifecycle.asLiveData

class BookmarkedDrugsRepository (
    private val dao: DrugInfoDao
) {
    suspend fun insertBookmarkedRepo(drug: DrugInfo) = dao.insert(drug)
    suspend fun deleteBookmarkedRepo(drug: DrugInfo) = dao.delete(drug)
    fun getAllBookmarkedRepos() = dao.getAllRepos()
}