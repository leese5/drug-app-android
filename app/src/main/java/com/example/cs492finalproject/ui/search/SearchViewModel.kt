package com.example.cs492finalproject.ui.search

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs492finalproject.DailyMedService
import com.example.cs492finalproject.DrugInfo
import com.example.cs492finalproject.DrugSearchResultsRepository
import com.example.cs492finalproject.LoadingStatus
import com.example.cs492finalproject.SearchHistoryDatabase
import kotlinx.coroutines.launch
import androidx.room.Room
import com.example.cs492finalproject.SearchHistory

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = DrugSearchResultsRepository(DailyMedService.create())
    private val db = Room.databaseBuilder(application, SearchHistoryDatabase::class.java, "app_database").build()
    private val searchHistoryDao = db.searchHistoryDao()

    private val _text = MutableLiveData<String>().apply {
        value = "This is search Fragment"
    }
    val text: LiveData<String> = _text

    private val _drugSearchResults = MutableLiveData<List<DrugInfo>?>(null)
    val drugSearchResults: LiveData<List<DrugInfo>?> = _drugSearchResults

    private val _loadingStatus = MutableLiveData<LoadingStatus>(LoadingStatus.SUCCESS)
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    private val _searchHistory = MutableLiveData<List<String>>()
    val searchHistory: LiveData<List<String>> = _searchHistory

    init {
        loadSearchHistory()
    }

    fun loadDrugSearchResults(query: String) {
        viewModelScope.launch {
            _loadingStatus.value = LoadingStatus.LOADING
            val result = repository.loadDrugSearch(query)
            _drugSearchResults.value = result.getOrNull()
            _error.value = result.exceptionOrNull()?.message
            _loadingStatus.value = when (result.isSuccess) {
                true -> LoadingStatus.SUCCESS
                false -> LoadingStatus.ERROR
            }
            Log.d("SearchQuery", "Result: " + result.toString())

            updateSearchHistory(query)
        }
    }

    private fun loadSearchHistory() {
        viewModelScope.launch {
            val history = searchHistoryDao.getRecentSearches().map { it.query }
            _searchHistory.postValue(history)
        }
    }

    private fun updateSearchHistory(query: String) {
        viewModelScope.launch {
            searchHistoryDao.insert(SearchHistory(query = query))
            searchHistoryDao.trimHistory()
            loadSearchHistory()
        }
    }
}