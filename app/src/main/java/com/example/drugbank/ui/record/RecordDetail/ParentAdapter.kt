package com.example.drugbank.ui.record.RecordDetail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.drugbank.R
import com.example.drugbank.respone.ProfileDetailRespone

class ParentAdapter(private val parentItemList: List<ProfileDetailRespone.ProfileDetail?>?)
    : RecyclerView.Adapter<ParentAdapter.ParentRecyclerView>()
{
        inner class ParentRecyclerView(item: View): RecyclerView.ViewHolder(item) {
            val ststus: TextView = item.findViewById(R.id.status_profileDetial)
            val layoutIncude: LinearLayout = item.findViewById(R.id.layout_includeProfileDetail)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentRecyclerView {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.base_profiledetailist, parent, false)
        return ParentRecyclerView(view)
    }

    override fun getItemCount(): Int {
        return parentItemList?.size!!
    }


    override fun onBindViewHolder(holder: ParentRecyclerView, position: Int) {
        val parentItem = parentItemList?.get(position)
        if (parentItem != null) {
            holder.ststus.text = parentItem.status
        }
        holder.layoutIncude.setOnClickListener {

        }
    }

}