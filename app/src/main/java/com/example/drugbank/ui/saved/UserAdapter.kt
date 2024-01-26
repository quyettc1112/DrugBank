package com.example.drugbank.ui.saved

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.drugbank.R
import com.example.drugbank.common.constant.Constant
import com.example.drugbank.data.model.User
import com.example.drugbank.databinding.BaseRecycleUserBinding
import com.squareup.picasso.Picasso

class UserAdapter(val userList: List<User>): RecyclerView.Adapter<UserAdapter.MainViewHolder>()
{
    inner class MainViewHolder(val itemBinding: BaseRecycleUserBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bindItem(user: User) {
            itemBinding.tvUserName.text = user.fullname
//            Picasso.get()
//                .load(user.imageIcon) // Assuming item.img is the URL string
//                .placeholder(user.imageIcon) // Optional: Placeholder image while loading
//                .error(user.imageIcon) // Optional: Error image to display on load failure
//                .into(itemBinding.ivUserAvatar)
            itemBinding.ivUserAvatar.setImageResource(R.drawable.avatar_1)
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
        return userList.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val task = userList[position]
        holder.bindItem(task)
    }


}