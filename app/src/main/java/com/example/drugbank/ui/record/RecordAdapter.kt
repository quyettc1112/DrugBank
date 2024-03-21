package com.example.drugbank.ui.record

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.drugbank.R
import com.example.drugbank.databinding.BaseProfileListBinding
import com.example.drugbank.respone.ProfileListRespone
import com.squareup.picasso.Picasso

class RecordAdapter( val context: Context): RecyclerView.Adapter<RecordAdapter.MainViewHolder>() {

    var onItemClick: ((ProfileListRespone.Content) -> Unit)? = null
    inner class MainViewHolder(val itemBinding: BaseProfileListBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bindItem(item: ProfileListRespone.Content) {
            Picasso.get()
                .load(item?.imageURL) // Assuming item.img is the URL string
                .placeholder(R.drawable.defultdrug_base) // Optional: Placeholder image while loading
                .error(R.drawable.defultdrug_base) // Optional: Error image to display on load failure
                .into(itemBinding.ivProductDetail)
//            if (item.imageURL != null) {
//                Glide.with(context) // Context
//                    .load(item.imageURL) // URL
//                    .placeholder(R.drawable.defultdrug_base) // Placeholder image
//                    .error(R.drawable.defultdrug_base) // Error image
//                    .into(itemBinding.ivProductDetail) // ImageView to load the image into
//            } else {
//                // Nếu URL là null, sử dụng ảnh mặc định
//                itemBinding.ivProductDetail.setImageResource(R.drawable.defultdrug_base)
//            }

           // itemBinding.ivProductDetail.setImageURI(item.imageURL)

            itemBinding.tvProfileName.text = "Name: ${item.title}"
            itemBinding.tvProfieCreate.text = item.createdOn

            if (item.status == "CLOSED") {
                itemBinding.tvProfileStatus.text = item.status
                itemBinding.tvProfileStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.red_light))
            }
            if (item.status == "PENDING") {
                itemBinding.tvProfileStatus.text = item.status
                itemBinding.tvProfileStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.icon_color_bottom_normal))
            }


        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            BaseProfileListBinding.inflate(
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

    private val differCallBack = object : DiffUtil.ItemCallback<ProfileListRespone.Content>() {
        override fun areItemsTheSame(
            oldItem: ProfileListRespone.Content,
            newItem: ProfileListRespone.Content
        ): Boolean {
            return  oldItem.profileId == newItem.profileId
        }

        override fun areContentsTheSame(
            oldItem: ProfileListRespone.Content,
            newItem: ProfileListRespone.Content
        ): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallBack)
}