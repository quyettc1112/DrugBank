package com.example.drugbank.ui.usermanager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.drugbank.R
import com.example.drugbank.base.dialog.ErrorDialog
import com.example.drugbank.data.model.User
import com.example.drugbank.databinding.BaseRecycleUserBinding
import com.example.drugbank.repository.Admin_UserM_Repository
import com.example.drugbank.respone.UserListResponse
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserManagerAdapter(context: Context, userRepository: Admin_UserM_Repository, token: String, fragment: UserManagerFragment): RecyclerView.Adapter<UserManagerAdapter.MainViewHolder>()
{
    var onItemClick: ((User) -> Unit)? = null
    val context: Context = context
    val userRepository:Admin_UserM_Repository = userRepository
    val token: String = token
    val fragment:UserManagerFragment = fragment
    inner class MainViewHolder(val itemBinding: BaseRecycleUserBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bindItem(user: User) {
            itemBinding.tvUserName.text = user.fullname
            itemBinding.tvRole.text = user.roleName
            itemBinding.tvEmail.text = user.email
            val backgroundResId = if (user.gender == 0) R.drawable.background_male else R.drawable.background_female
            itemBinding.backgroundGender.setBackgroundResource(backgroundResId)

            if (user.isActive.equals("Deactivate")) {
                itemBinding.ivUserAvatar.setImageResource(R.drawable.baseline_block_24)
            }
            Picasso.get()
                .load(user.avatar) // Assuming item.img is the URL string
                .placeholder(R.drawable.user_general) // Optional: Placeholder image while loading
                .error(R.drawable.user_general) // Optional: Error image to display on load failure
                .into(itemBinding.ivUserAvatar)

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

        holder.itemView.findViewById<ImageView>(R.id.im_Btnsetting).setOnClickListener {
            showActiveDialog(Item)
        }
    }

    private fun showActiveDialog(user: User) {
        val inflater = LayoutInflater.from(context)
        val dialogView: View = inflater.inflate(R.layout.dialog_active_user, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(dialogView)
        val dialog = builder.create()

        val ivUserAvatar = dialogView.findViewById<CircleImageView>(R.id.ivUserAvatar)
        val tv_userName = dialogView.findViewById<TextView>(R.id.tv_userName)
        val btnOK = dialogView.findViewById<AppCompatButton>(R.id.btnOK)
        val activeName = dialogView.findViewById<AutoCompleteTextView>(R.id.atc_ActiveList)
        val activeList = context.resources.getStringArray(R.array.Active_user_adapter)
        val arrayApderActive = ArrayAdapter(context, R.layout.dropdown_menu, activeList)
        activeName.setAdapter(arrayApderActive)

        if (user.isActive.equals("Deactivate")) {
            activeName.setHint("Deactivate")
        } else activeName.setHint("Active")




//        val avatarResId = if (user.gender == 0) R.drawable.anh_2 else R.drawable.anh_3
//        ivUserAvatar.setImageResource(avatarResId)
        Picasso.get()
            .load(user.avatar) // Assuming item.img is the URL string
            .placeholder(R.drawable.user_general) // Optional: Placeholder image while loading
            .error(R.drawable.user_general) // Optional: Error image to display on load failure
            .into(ivUserAvatar)

        tv_userName.text = user.fullname
        //activeName.setText(user.isActive.toString())


        activeName.setOnItemClickListener { _, _, position, _ ->
            if (activeList[position] == "Active") {
                btnOK.setOnClickListener {
                    userRepository.activateUser(
                        authorization =  "Bearer ${token}",
                        email = user.email
                    ).enqueue(object : Callback<UserListResponse.User> {
                        override fun onResponse(
                            call: Call<UserListResponse.User>,
                            response: Response<UserListResponse.User>
                        ) {
                            fragment.CallUserList()
                            dialog.dismiss()

                            if (response.code() == 403) {
                                val errorDialog = ErrorDialog(
                                    context = context,
                                    errorContent = "Forbiden Error",
                                    textButton = "Try Again"
                                )
                                errorDialog.show()
                            }
                        }
                        override fun onFailure(call: Call<UserListResponse.User>, t: Throwable) {
                            dialog.dismiss()
                            val errorDialog = ErrorDialog(
                                context = context,
                                errorContent = t.message.toString(),
                                textButton = "Try Again"
                            )
                            errorDialog.show()
                        }
                    })


                }
            }
            if (activeList[position] == "Deactivate") {
                btnOK.setOnClickListener {
                    userRepository.deactivateUser(
                        authorization =  "Bearer ${token}",
                        email = user.email
                    ).enqueue(object : Callback<UserListResponse.User> {
                        override fun onResponse(
                            call: Call<UserListResponse.User>,
                            response: Response<UserListResponse.User>
                        ) {
                            fragment.CallUserList()
                            dialog.dismiss()
                        }
                        override fun onFailure(call: Call<UserListResponse.User>, t: Throwable) {
                            dialog.dismiss()
                            val errorDialog = ErrorDialog(
                                context = context,
                                errorContent = t.message.toString(),
                                textButton = "Try Again"
                            )
                            errorDialog.show()
                        }
                    })
                }

            }

        }

        dialog.show()

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