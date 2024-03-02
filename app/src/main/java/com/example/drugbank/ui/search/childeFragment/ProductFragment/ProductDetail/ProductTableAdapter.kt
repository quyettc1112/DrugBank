package com.example.drugbank.ui.search.childeFragment.ProductFragment.ProductDetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.drugbank.R
import com.example.drugbank.data.model.DrugIn

class ProductTableAdapter(private val data: List<DrugIn>):
    RecyclerView.Adapter<ProductTableAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val drugIn_Name: TextView = itemView.findViewById(R.id.drugIn_name)
            val drugIn_strength: TextView = itemView.findViewById(R.id.drugIn_strength)
            val drugIn_strengthNumber: TextView = itemView.findViewById(R.id.drugIn_strengthNumber)
            val drugIn_strengthUnit: TextView = itemView.findViewById(R.id.drugIn_strengthUnit)
            val drug_clinical: TextView = itemView.findViewById(R.id.drug_clinical)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.background_table_row_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = data.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.drugIn_Name.text = item.name
        holder.drugIn_strength.text = item.strength
        holder.drugIn_strengthUnit.text = item.strengthUnit
        holder.drugIn_strengthNumber.text = item.strengthNumber
        holder.drug_clinical.text = item.clinicallyRelevant

    }

}