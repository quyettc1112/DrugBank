package com.example.drugbank.ui.saved

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.SearchView.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.drugbank.R
import com.example.drugbank.common.BaseAPI.RetrofitClient
import com.example.drugbank.common.Token.TokenManager
import com.example.drugbank.data.model.User
import com.example.drugbank.databinding.FragmentSavedBinding
import com.example.drugbank.respone.UserListResponse
import dagger.hilt.android.AndroidEntryPoint
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
            Toast.makeText(requireContext(), "Click", Toast.LENGTH_SHORT).show()
        }

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
                    } else {
                        // If the query is empty, reset the list to its original state
                        _userAdapter.differ.submitList(originalList) // Assuming you have the original list stored
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