package com.example.drugbank.ui.search.childeFragment.ProductFragment.ProductDetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.drugbank.R
import com.example.drugbank.data.model.Authorities

class ProductDetail_Authorites_Adapter(private val data: List<Authorities>):
    RecyclerView.Adapter<ProductDetail_Authorites_Adapter.ViewHolder>()
{

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val author_certificateName: TextView = itemView.findViewById(R.id.author_certificateName)
        val author_countryId: TextView = itemView.findViewById(R.id.author_countryId)
        val author_countryName: TextView = itemView.findViewById(R.id.author_countryName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.base_authorities_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.author_countryId.text = item.countryId.toString()
        holder.author_countryName.text = item.countryName.toString()
        holder.author_certificateName.text = item.certificateName.toString()
    }
}