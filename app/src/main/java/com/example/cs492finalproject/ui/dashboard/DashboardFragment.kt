package com.example.cs492finalproject.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs492finalproject.DrugInfo
import com.example.cs492finalproject.R
import com.example.cs492finalproject.databinding.FragmentDashboardBinding
import com.example.cs492finalproject.ui.DrugInfoFragmentDirections
import com.example.cs492finalproject.ui.search.DrugSearchResultsAdapter

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private lateinit var bookmarkedReposRV: RecyclerView
    private val adapter = DrugSearchResultsAdapter(::onDrugSearchResultItemClick)
    private val viewModel: BookmarkedReposViewModel by viewModels()

    private val _text = MutableLiveData<String>()
    val text: LiveData<String> = _text
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookmarkedReposRV = view.findViewById(R.id.rv_bookmarked_repos)
        bookmarkedReposRV.layoutManager = LinearLayoutManager(requireContext())
        bookmarkedReposRV.setHasFixedSize(true)
        bookmarkedReposRV.adapter = adapter

        viewModel.bookmarkedRepos.observe(viewLifecycleOwner) { bookmarkedRepos ->
            adapter.updateResultsList(bookmarkedRepos)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onDrugSearchResultItemClick(drug: DrugInfo) {
        Log.d("DrugInfo", "drug clicked" + drug.title)
        val directions = DrugInfoFragmentDirections.navigateToDrugInfo(drug)
        findNavController().navigate(directions)
    }
}