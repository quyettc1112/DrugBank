package com.example.drugbank.ui.record.RecordDetail

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.drugbank.R
import com.example.drugbank.common.constant.Constant
import com.example.drugbank.databinding.BaseProfileListBinding
import com.example.drugbank.databinding.BaseProfiledetailistBinding
import com.example.drugbank.respone.ProfileDetailRespone
import com.example.drugbank.respone.ProfileListRespone
import com.example.drugbank.ui.activity.main.MainActivity
import com.example.drugbank.ui.record.RecordAdapter
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso

class ProfileDetailAdapter(private val parentItemList: List<ProfileDetailRespone.ProfileDetail?>?,  val context: Context, val activity: MainActivity): RecyclerView.Adapter<ProfileDetailAdapter.MainViewHolder>() {

    var onItemClick: ((ProfileDetailRespone.ProfileDetail) -> Unit)? = null
    inner class MainViewHolder(val itemBinding: BaseProfiledetailistBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bindItem(item: ProfileDetailRespone.ProfileDetail?) {
            itemBinding.statusProfileDetial.text = item?.status.toString()
            if (item?.status == "REJECTED") {
                itemBinding.statusProfileDetial.setBackgroundColor(ContextCompat.getColor(context, R.color.red_light))
            } else  itemBinding.statusProfileDetial.setBackgroundColor(ContextCompat.getColor(context, R.color.i_blue))

            itemBinding.nameProfileDetail.text = item?.productResponseDTO?.name.toString()
                if (item != null) {
                    Picasso.get()
                        .load(item!!.productResponseDTO?.image.toString()) // Assuming item.img is the URL string
                        .placeholder(R.drawable.loadingsim) // Optional: Placeholder image while loading
                        .error(R.drawable.defultdrug_base) // Optional: Error image to display on load failure
                        .into(itemBinding.imageView2)
                }
            itemBinding.layout.productName.text = item?.productResponseDTO?.name.toString()
            itemBinding.layout.productId.text = item?.productResponseDTO?.id.toString()
            itemBinding.layout.productLabeller.text = item?.productResponseDTO?.name.toString()
            itemBinding.layout.prouductNameSmall.text = item?.productResponseDTO?.name.toString()
            itemBinding.layout.productPrename.text = item?.productResponseDTO?.prescriptionName.toString()








        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            BaseProfiledetailistBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return parentItemList?.size ?: 0
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val Item = parentItemList?.get(position)
        holder.bindItem(Item)
        holder.itemView.setOnClickListener {
            if (Item != null) {
                onItemClick?.invoke(Item)
                val layoutHolder = holder.itemView.findViewById<LinearLayout>(R.id.layout_includeProfileDetail)
                layoutHolder.visibility = if (layoutHolder.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                holder.itemBinding.layout.btnViewProductDetail.setOnClickListener {
                    val navController = activity.findNavController(R.id.nav_host_fragment_activity_main)
                    val sharedPreferences = activity.getSharedPreferences(Constant.CURRENT_PRODUCT_ID, Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putInt(Constant.CURRENT_PRODUCT_ID_VALUE, Item.productResponseDTO?.id!!)
                    editor.apply()
                    navController.navigate(Constant.getNavSeleted(5))
                }
            }
        }
    }

}