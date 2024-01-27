package com.example.drugbank.ui.saved

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.RadioButton
import android.widget.SearchView
import android.widget.SearchView.*
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.drugbank.R
import com.example.drugbank.common.BaseAPI.RetrofitClient
import com.example.drugbank.common.Resource.Screen
import com.example.drugbank.common.Token.TokenManager
import com.example.drugbank.data.model.User
import com.example.drugbank.databinding.FragmentSavedBinding
import com.example.drugbank.respone.UserListResponse
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class SavedFragment : Fragment() {

   // private lateinit var binding:ActivityMainBinding
    private lateinit var _binding: FragmentSavedBinding
    private lateinit var _userAdapter: UserAdapter

    lateinit var _viewModel: SavedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedBinding.inflate(inflater, container, false)
        _viewModel = ViewModelProvider(this).get(SavedViewModel::class.java)
        setUpComboBoxWithViewmodel()

        return _binding.root
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _userAdapter = UserAdapter()
        onItemClickAdapter()
        val OgirginList = _userAdapter.differ.currentList
        searchViewOnQuery()
        val itemTouchHelper = ItemTouchHelper(_userAdapter.getSimpleCallBack())
        itemTouchHelper.attachToRecyclerView(_binding.rclListUser)
        CallUserList()
    }

    private fun CallUserList() {
        val tokenManager = TokenManager(requireContext())
        RetrofitClient.instance_User.getPageableUser(
            "Bearer ${tokenManager.getAccessToken()}",
            pageNo = 0,
            pageSize = 15,
            sortField = "id",
            sortOrder = "asc",
            roleName = _viewModel.selectedRole.value,
            status = _viewModel.selectedActive.value,
            gender = _viewModel.selectedGender.value
        ).enqueue(object : Callback<UserListResponse> {
            override fun onResponse(
                call: Call<UserListResponse>,
                response: Response<UserListResponse>
            ) {
                if (response.isSuccessful) {
                    val userResponse: UserListResponse? = response.body()
                    val userList: List<User> = userResponse?.content?.map { user ->
                        User(
                            id = user.id,
                            username = user.username,
                            email = user.email,
                            fullname = user.fullname,
                            dayOfBirth = user.dayOfBirth,
                            gender = user.gender,
                            roleName = user.roleName,
                            isActive = user.isActive
                        )
                    } ?: emptyList()
                    _userAdapter.differ.submitList(userList)
                    _binding.rclListUser.adapter = _userAdapter
                } else {
                    println("Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                println("Failed to make API call: ${t.message}")
            }
        })
    }

    private fun onItemClickAdapter() {
        _userAdapter.onItemClick = {
            showUserInfoDialog(it)
        }

    }

    private fun showUserInfoDialog(user: User) {
        val dialogBinding = layoutInflater.inflate(R.layout.dialog_user_info, null)
        val myDialog = Dialog(requireContext())


        val username = dialogBinding.findViewById<TextView>(R.id.tv_userName)
        val id   = dialogBinding.findViewById<TextView>(R.id.tv_id)
        val etEmail = dialogBinding.findViewById<TextInputEditText>(R.id.etEmail_userInfo)
        val et_fullname = dialogBinding.findViewById<TextInputEditText>(R.id.et_fullname)
        val et_dateofbirth = dialogBinding.findViewById<EditText>(R.id.et_dateofbirth)
        val ivUserAvatar = dialogBinding.findViewById<CircleImageView>(R.id.ivUserAvatar)



        val male = dialogBinding.findViewById<RadioButton>(R.id.rdo_btn_male)
        val female = dialogBinding.findViewById<RadioButton>(R.id.rdo_btn_female)
        male.isChecked = user.gender == 0
        female.isChecked = !male.isChecked


        username.text = user.username
        id.text ="ID: "+ user.id.toString()
        etEmail.setText(user.email)
        et_fullname.setText(user.fullname)
        et_dateofbirth.setText(user.dayOfBirth)
        val avatarResId = if (user.gender == 0) R.drawable.anh_2 else R.drawable.anh_3
        ivUserAvatar.setImageResource(avatarResId)


        val rolename = dialogBinding.findViewById<AutoCompleteTextView>(R.id.atc_roleListCombo_info)
        val activeName = dialogBinding.findViewById<AutoCompleteTextView>(R.id.atc_ActiveList)

        val rolelist = resources.getStringArray(R.array.RoleName)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu, rolelist)
        rolename.setAdapter(arrayAdapter)
        rolename.setText(user.roleName)
        if (user.roleName =="SUPERADMIN") {
            rolename.setText("SUPER ADMIN")
        } else rolename.setText(user.roleName)
//        for (i in 0 until rolelist.size) {
//            // So sánh đoạn string bạn truyền vào với từng đoạn string trong adapter
//            if (user.roleName == rolelist[i]) {
//                // Đặt vị trí của bộ chọn xuống thành đoạn string khớp
//                rolename.setSelection(i)
//                break
//            }
//        }

        val activeList = resources.getStringArray(R.array.Active)
        val arrayApderActive = ArrayAdapter(requireContext(), R.layout.dropdown_menu, activeList)
        activeName.setAdapter(arrayApderActive)



        myDialog.setContentView(dialogBinding)
        myDialog.setCancelable(true)
        myDialog.window?.setLayout(Screen.width, Screen.height)
       // myDialog.window?.setBackgroundDrawable(ColorDrawable(requireContext().getColor(R.color.zxing_transparent)))
        myDialog.show()


    }

    private fun searchViewOnQuery() {
        val searchView = _binding.searchView
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object :  androidx.appcompat.widget.SearchView.OnQueryTextListener
            {
                val originalList = _viewModel.userList.value ?: emptyList()
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (!newText.isNullOrBlank()) {
                        // Access the current list from the adapter's differ
                        val currentList = _userAdapter.differ.currentList

                        // Filter the list based on the new query
                        val filteredList = currentList.filter { item ->
                            // Replace this with your actual filtering logic
                            item.fullname.contains(newText, ignoreCase = true)
                        }
                        // Submit the filtered list to the adapter
                        _userAdapter.differ.submitList(filteredList)
                    }
                    if (newText?.length == 0) {
                        CallUserList()
                    }
                    return true
                }

            })
    }
    private fun setUpComboBoxWithViewmodel() {
        val rolelist = resources.getStringArray(R.array.RoleName)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu, rolelist)
        _binding.atcRoleListCombo.setAdapter(arrayAdapter)

        _binding.atcRoleListCombo.setOnItemClickListener { _, _, position, _ ->
            _viewModel.selectedRole.value = rolelist[position]
            if (rolelist[position] == "SUPER ADMIN") {
                _viewModel.selectedRole.value = "SUPERADMIN"
            }
            CallUserList()
        }

        val genderList = resources.getStringArray(R.array.Gender)
        val arrayApderGender = ArrayAdapter(requireContext(), R.layout.dropdown_menu, genderList)
        _binding.atcGenderList.setAdapter(arrayApderGender)

        _binding.atcGenderList.setOnItemClickListener { _, _, position, _ ->
            if (genderList[position] == "ALL") {
                _viewModel.selectedGender.value = null
            }
            if (genderList[position] == "Male") {
                _viewModel.selectedGender.value = 0
            }
            if (genderList[position] == "Female") {
                _viewModel.selectedGender.value = 1
            }
            CallUserList()
        }

        val activeList = resources.getStringArray(R.array.Active)
        val arrayApderActive = ArrayAdapter(requireContext(), R.layout.dropdown_menu, activeList)
        _binding.atcActiveList.setAdapter(arrayApderActive)
        _binding.atcActiveList.setOnItemClickListener { _, _, position, _ ->
            _viewModel.selectedActive.value = activeList[position]
            if (activeList[position] == "ALL") {
                _viewModel.selectedActive.value = null
            }
            CallUserList()
        }


    }





}