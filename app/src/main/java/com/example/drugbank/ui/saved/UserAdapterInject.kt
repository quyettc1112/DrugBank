package com.example.drugbank.ui.saved

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.drugbank.databinding.BaseRecycleUserBinding
import com.example.drugbank.respone.UserListResponse
import javax.inject.Inject

class UserAdapterInject @Inject() constructor():
    PagingDataAdapter<UserListResponse.User, UserAdapterInject.ViewHolder>(differCallback)
{
    private lateinit var binding: BaseRecycleUserBinding
    private lateinit var context: Context
    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UserListResponse.User) {
            binding.apply {
               tvUserName.text = item.fullname
            }
        }
    }

    override fun onBindViewHolder(holder: UserAdapterInject.ViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
        holder.setIsRecyclable(false)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserAdapterInject.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = BaseRecycleUserBinding.inflate(inflater, parent, false)
        context = parent.context
        return ViewHolder()
    }

    companion object {
        val differCallback = object : DiffUtil.ItemCallback<UserListResponse.User>() {
            override fun areItemsTheSame(oldItem: UserListResponse.User, newItem: UserListResponse.User): Boolean {
                return oldItem.id == oldItem.id
            }

            override fun areContentsTheSame(oldItem: UserListResponse.User, newItem: UserListResponse.User): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var onItemClickListener: ((UserListResponse.User) -> Unit)? = null

    fun setOnItemClickListener(listener: (UserListResponse.User) -> Unit) {
        onItemClickListener = listener
    }
}