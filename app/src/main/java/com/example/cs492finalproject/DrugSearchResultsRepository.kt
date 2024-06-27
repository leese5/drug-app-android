package com.example.cs492finalproject

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DrugSearchResultsRepository(
    private val service: DailyMedService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun loadDrugSearch(query: String): Result<List<DrugInfo>> =
        withContext(ioDispatcher) {
        try {
            val response = service.getDrugData( drugName = query)
            if (response.isSuccessful) {
                Result.success(response.body()?.data ?: listOf())
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}