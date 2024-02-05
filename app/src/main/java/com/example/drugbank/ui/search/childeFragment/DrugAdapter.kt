package com.example.drugbank.ui.search.childeFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.drugbank.R
import com.example.drugbank.data.model.Drug
import com.example.drugbank.data.model.User
import com.example.drugbank.databinding.BaseRecycleDrugBinding
import com.example.drugbank.databinding.BaseRecycleUserBinding

class DrugAdapter(): RecyclerView.Adapter<DrugAdapter.MainViewHolder>() {

    var onItemClick: ((Drug) -> Unit)? = null
    inner class MainViewHolder(val itemBinding: BaseRecycleDrugBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bindItem(drug: Drug) {
            itemBinding.tvDrugname.text = drug.name
            itemBinding.tvSimpleDescription.text = drug.simpleDescription
            itemBinding.tvType.text = "Type:" + drug.type
            itemBinding.tvApprovalStatus.text = "ApprovalStatus: " + drug.approvalStatus
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrugAdapter.MainViewHolder {
        return MainViewHolder(
            BaseRecycleDrugBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DrugAdapter.MainViewHolder, position: Int) {
        val Item = differ.currentList[position]
        holder.bindItem(Item)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(Item)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    private val differCallBack = object : DiffUtil.ItemCallback<Drug>() {
        override fun areItemsTheSame(oldItem: Drug, newItem: Drug): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Drug, newItem: Drug): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallBack)



}