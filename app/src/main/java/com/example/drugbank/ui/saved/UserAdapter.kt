package com.example.drugbank.ui.saved

import android.content.Context
import android.os.Parcel
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.drugbank.R
import com.example.drugbank.common.constant.Constant
import com.example.drugbank.data.model.User
import com.example.drugbank.databinding.BaseRecycleUserBinding
import com.google.android.gms.drive.query.Filter
import com.google.android.gms.drive.query.internal.zzj
import com.squareup.picasso.Picasso
import kotlin.coroutines.coroutineContext

class UserAdapter(): RecyclerView.Adapter<UserAdapter.MainViewHolder>()
{
    var onItemClick: ((User) -> Unit)? = null
    inner class MainViewHolder(val itemBinding: BaseRecycleUserBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bindItem(user: User) {
            itemBinding.tvUserName.text = user.fullname
            itemBinding.tvRole.text = user.roleName
            itemBinding.tvEmail.text = user.email
            if (user.gender == 0) {
                itemBinding.backgroundGender.setBackgroundResource(R.drawable.background_male)
                itemBinding.ivUserAvatar.setImageResource(R.drawable.anh_2)
            } else {
                itemBinding.backgroundGender.setBackgroundResource(R.drawable.background_female)
                itemBinding.ivUserAvatar.setImageResource(R.drawable.anh_3)
            }

            if (user.isActive.equals("Deactivate")) {
                itemBinding.ivUserAvatar.setImageResource(R.drawable.baseline_block_24)

            }
//            Picasso.get()
//                .load(user.imageIcon) // Assuming item.img is the URL string
//                .placeholder(user.imageIcon) // Optional: Placeholder image while loading
//                .error(user.imageIcon) // Optional: Error image to display on load failure
//                .into(itemBinding.ivUserAvatar)

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            BaseRecycleUserBinding.inflate(
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
    private val differCallBack = object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    inner class SimpleCallBack :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition

        }
    }

    fun getSimpleCallBack(): SimpleCallBack {
        return SimpleCallBack()
    }




}