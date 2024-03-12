package com.example.drugbank.ui.search.childeFragment.ProductFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.drugbank.R
import com.example.drugbank.databinding.BaseProductListRecycleBinding
import com.example.drugbank.respone.ProductListRespone
import com.squareup.picasso.Picasso

class ProductAdapter(): RecyclerView.Adapter<ProductAdapter.MainViewHolder>() {

    var onItemClick: ((ProductListRespone.Content) -> Unit)? = null
        inner class MainViewHolder(val itemBinding: BaseProductListRecycleBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bindItem(item: ProductListRespone.Content) {
            Picasso.get()
                .load(item?.image) // Assuming item.img is the URL string
                .placeholder(R.drawable.dafult_product_img) // Optional: Placeholder image while loading
                .error(R.drawable.loadingsim) // Optional: Error image to display on load failure
                .into(itemBinding.ivImgProduct)
          //  itemBinding.ivImgProduct.setImageResource(R.drawable.dafult_product_img)
            itemBinding.tvCategory.text = item.category
            itemBinding.tvRoute.text = item.route
            itemBinding.tvProductname.text = item.name
            itemBinding.tvPrescripsionname.text = item.prescriptionName

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            BaseProductListRecycleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val Item = differ.currentList[position]
        holder.bindItem(Item)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(Item)
        }
    }


    private val differCallBack = object : DiffUtil.ItemCallback<ProductListRespone.Content>() {
        override fun areItemsTheSame(
            oldItem: ProductListRespone.Content,
            newItem: ProductListRespone.Content
        ): Boolean {
            return  oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ProductListRespone.Content,
            newItem: ProductListRespone.Content
        ): Boolean {
            return oldItem == newItem
        }


    }
    val differ = AsyncListDiffer(this, differCallBack)
}