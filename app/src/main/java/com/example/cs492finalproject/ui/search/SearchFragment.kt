package com.example.cs492finalproject.ui.search

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs492finalproject.DrugInfo
import com.example.cs492finalproject.LoadingStatus
import com.example.cs492finalproject.R
import com.example.cs492finalproject.SearchHistoryAdapter
import com.example.cs492finalproject.databinding.FragmentSearchBinding
import com.example.cs492finalproject.ui.DrugInfoFragmentDirections
import com.google.android.material.progressindicator.CircularProgressIndicator

class SearchFragment : Fragment(R.layout.fragment_search) {
    private val viewModel: SearchViewModel by viewModels()
    private val adapter = DrugSearchResultsAdapter(::onDrugSearchResultItemClick)
    private lateinit var searchResultsListRV: RecyclerView
    private lateinit var errorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator
    private lateinit var searchHistoryRV: RecyclerView
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter

    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchBtn: Button = view.findViewById(R.id.btn_search)
        val searchBoxET: EditText = view.findViewById(R.id.et_search_box)
        errorTV = view.findViewById(R.id.tv_search_error)
        loadingIndicator = view.findViewById(R.id.loading_indicator)

        searchResultsListRV = view.findViewById(R.id.rv_search_results)
        searchResultsListRV.layoutManager = LinearLayoutManager(requireContext())
        searchResultsListRV.setHasFixedSize(true)

        // Setup for displaying search results (if you have an adapter for this)
        val adapter = DrugSearchResultsAdapter(::onDrugSearchResultItemClick)
        searchResultsListRV.adapter = adapter

        // Initialize and setup search history RecyclerView
        searchHistoryRV = view.findViewById(R.id.rv_search_history)
        searchHistoryAdapter = SearchHistoryAdapter(emptyList()) { query ->
            searchBoxET.setText(query)
            viewModel.loadDrugSearchResults(query)
        }
        searchHistoryRV.layoutManager = LinearLayoutManager(requireContext())
        searchHistoryRV.adapter = searchHistoryAdapter

        viewModel.loadingStatus.observe(viewLifecycleOwner) {
            loadingStatus ->
            when (loadingStatus) {
                LoadingStatus.LOADING -> {
                    searchResultsListRV.visibility = View.INVISIBLE
                    loadingIndicator.visibility = View.VISIBLE
                    errorTV.visibility = View.INVISIBLE
                }
                LoadingStatus.ERROR -> {
                    searchResultsListRV.visibility = View.INVISIBLE
                    loadingIndicator.visibility = View.INVISIBLE
                    errorTV.visibility = View.VISIBLE
                }
                else -> {
                    searchResultsListRV.visibility = View.VISIBLE
                    loadingIndicator.visibility = View.INVISIBLE
                    errorTV.visibility = View.INVISIBLE
                }
            }
        }

        viewModel.drugSearchResults.observe(viewLifecycleOwner) {
            drugSearchResults -> adapter.updateResultsList(drugSearchResults)
        }

        searchHistoryRV.layoutManager = LinearLayoutManager(requireContext())
        searchHistoryAdapter = SearchHistoryAdapter(emptyList()) { query ->
            viewModel.loadDrugSearchResults(query)
        }
        searchHistoryRV.adapter = searchHistoryAdapter

        viewModel.searchHistory.observe(viewLifecycleOwner) { history ->
            searchHistoryAdapter.updateData(history)
        }

        searchBtn.setOnClickListener {
            Log.d("SearchQuery", "Search button was clicked")
            val query = searchBoxET.text.toString()
            if (!TextUtils.isEmpty(query)) {
                Log.d("SearchQuery", "Search query: $query")
                viewModel.loadDrugSearchResults(query)
            }
        }
    }

    private fun onSearchHistoryItemClick(drugInfo: DrugInfo) {
        viewModel.loadDrugSearchResults(drugInfo.title)
    }

    private fun onDrugSearchResultItemClick(drug: DrugInfo) {
        Log.d("DrugInfo", "drug clicked" + drug.title)
        val directions = DrugInfoFragmentDirections.navigateToDrugInfo(drug)
        findNavController().navigate(directions)
    }
}