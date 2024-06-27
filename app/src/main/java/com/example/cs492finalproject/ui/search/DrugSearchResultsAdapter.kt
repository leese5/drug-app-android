package com.example.cs492finalproject.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cs492finalproject.DrugInfo
import com.example.cs492finalproject.R

class DrugSearchResultsAdapter(
    private val onDrugSearchResultItemClick: (DrugInfo) -> Unit
) : RecyclerView.Adapter<DrugSearchResultsAdapter.DrugSearchResultItemViewHolder>() {
    private var drugSearchResultsList = listOf<DrugInfo>()

    fun updateResultsList(newResultsList: List<DrugInfo>?) {
        notifyItemRangeRemoved(0, drugSearchResultsList.size)
        drugSearchResultsList = newResultsList ?: listOf()
        notifyItemRangeInserted(0, drugSearchResultsList.size)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrugSearchResultItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.drug_list_item, parent, false)
        return  DrugSearchResultItemViewHolder(itemView, onDrugSearchResultItemClick)
    }

    override fun getItemCount() = drugSearchResultsList.size

    override fun onBindViewHolder(holder: DrugSearchResultItemViewHolder, position: Int) {
        holder.bind(drugSearchResultsList[position])
    }
    class DrugSearchResultItemViewHolder(itemView: View, onClick: (DrugInfo) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val nameTV: TextView = itemView.findViewById(R.id.tv_drug_name)
        private val publishedDateTV: TextView = itemView.findViewById(R.id.tv_drug_description)
        private var currentDrugResultSearchItem: DrugInfo? = null

        init {
            itemView.setOnClickListener {
                currentDrugResultSearchItem?.let(onClick)
            }
        }

        fun bind(drugSearchResultItem: DrugInfo) {
            currentDrugResultSearchItem = drugSearchResultItem
            nameTV.text = drugSearchResultItem.title
            publishedDateTV.text = "Published Date: ${drugSearchResultItem.publishedDate}"
        }

    }
}