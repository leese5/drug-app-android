package com.example.cs492finalproject.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.cs492finalproject.AppDatabase
import com.example.cs492finalproject.BookmarkedDrugsRepository
import com.example.cs492finalproject.DrugInfo
import kotlinx.coroutines.launch

class BookmarkedReposViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = BookmarkedDrugsRepository(
        AppDatabase.getInstance(application).drugInfoDao()
    )

    val bookmarkedRepos = repository.getAllBookmarkedRepos().asLiveData()

    fun addBookmarkedRepo(drug: DrugInfo) {
        viewModelScope.launch {
            repository.insertBookmarkedRepo(drug)
        }
    }

    fun removeBookmarkedRepo(drug: DrugInfo) {
        viewModelScope.launch {
            repository.deleteBookmarkedRepo(drug)
        }
    }
}