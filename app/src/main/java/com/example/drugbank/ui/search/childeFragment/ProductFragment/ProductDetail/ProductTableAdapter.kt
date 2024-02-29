package com.example.drugbank.ui.search.childeFragment.ProductFragment.ProductDetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.drugbank.R
import com.example.drugbank.data.model.Drug

class ProductTableAdapter(private val data: ArrayList<String>):
    RecyclerView.Adapter<ProductTableAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
//        val textViewDetail: TextView = itemView.findViewById(R.id.textViewDetail)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.background_table_row_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = data.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
//        holder.textViewName.text = item.first
//        holder.textViewDetail.text = item.second
    }

}