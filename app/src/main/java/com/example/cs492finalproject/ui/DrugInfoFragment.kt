package com.example.cs492finalproject.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.cs492finalproject.DailyMedService
import com.example.cs492finalproject.DrugInfo
import com.example.cs492finalproject.PackagingData
import com.example.cs492finalproject.R
import com.example.cs492finalproject.ui.dashboard.BookmarkedReposViewModel
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import androidx.lifecycle.lifecycleScope
import com.example.cs492finalproject.PackagingDataProducts

class DrugInfoFragment : Fragment(R.layout.fragment_drug_info) {
    private val args: DrugInfoFragmentArgs by navArgs()
    private val viewModel: BookmarkedReposViewModel by viewModels()
    private var isBookmarked = false

    private val dailyMedService: DailyMedService by lazy {
        Retrofit.Builder()
            .baseUrl("https://dailymed.nlm.nih.gov/dailymed/services/v2/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(DailyMedService::class.java)
    }

    private lateinit var bookmarkMenuItem: MenuItem
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val openBrowserBtn: Button = view.findViewById(R.id.open_browser_button)
        val bookmarkButton: ImageButton = view.findViewById(R.id.bookmark_button)


        checkBookmarkStatus(args.drug)

        openBrowserBtn.setOnClickListener {
            Log.d("OpenBrowser", "Open browser button clicked, url: " + viewDrugInBrowser())
        }

        // Set initial bookmark icon based on the bookmark status
        updateBookmarkIcon(bookmarkButton)

        bookmarkButton.setOnClickListener {
            isBookmarked = !isBookmarked
            updateBookmarkIcon(bookmarkButton)

            // Add or remove drug from the database based on the bookmark status
            if (isBookmarked) {
                viewModel.addBookmarkedRepo(args.drug)
            } else {
                viewModel.removeBookmarkedRepo(args.drug)
            }
        }
        fetchPackagingInfo(args.drug.setId)
    }

    private fun fetchPackagingInfo(setId: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = dailyMedService.getPackagingData(setId)
                if (response.isSuccessful && response.body() != null) {
                    val packagingData = response.body()!!.data // Accessing the data object
                    val products = packagingData.products // Now accessing the products list
                    val packagingInfoTextView: TextView = view?.findViewById(R.id.packaging_info_text_view) ?: return@launch
                    displayPackagingInfo(products, packagingInfoTextView) // Assuming display logic is adapted accordingly
                } else {
                    Log.e("DrugInfoFragment", "Failed to fetch packaging data: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("DrugInfoFragment", "Error fetching packaging data", e)
            }
        }
    }

    private fun displayPackagingInfo(products: List<PackagingDataProducts>, packagingInfoTextView: TextView) {
        // Build a display text string using information from each product.
        val displayText = products.joinToString("\n") { product ->
            // Assuming each product has a name and perhaps other details you want to include.
            // Adjust the displayed details according to your specific needs.
            "Product Name: ${product.productName}" +
                    "\nGeneric Name: ${product.productNameGeneric}" +
                    "\nCode: ${product.productCode}" +
                    "\nPackaging: ${product.packaging.joinToString { pkg -> "${pkg.ndc} - ${pkg.packageDescriptions.joinToString()}" }}" +
                    "\nActive Ingredients: ${product.activeIngredients.joinToString { ing -> "${ing.name}: ${ing.strength}" }}"
        }

        // Set the constructed display text to the TextView.
        packagingInfoTextView.text = displayText
    }

    private fun viewDrugInBrowser(): String {
        val setid = Uri.parse(args.drug.setId)
        val baseUrl = "https://dailymed.nlm.nih.gov/dailymed/drugInfo.cfm?setid="
        val url = baseUrl + setid
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {

        }
        return url
    }

    private fun checkBookmarkStatus(drug: DrugInfo) {
        viewModel.bookmarkedRepos.observe(viewLifecycleOwner) { bookmarkedDrugs ->
            isBookmarked = bookmarkedDrugs.any { it.setId == drug.setId }
            updateBookmarkIcon(view?.findViewById(R.id.bookmark_button) ?: return@observe)
        }
    }


    private fun updateBookmarkIcon(bookmarkButton: ImageButton) {
        val bookmarkIcon = if (isBookmarked) {
            R.drawable.ic_action_bookmark_off
        } else {
            R.drawable.ic_action_bookmark_off
        }
        bookmarkButton.setImageResource(bookmarkIcon)
    }
}