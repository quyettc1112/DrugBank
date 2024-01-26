package com.example.drugbank.ui.saved

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.drugbank.R
import com.example.drugbank.common.BaseAPI.RetrofitClient
import com.example.drugbank.common.Token.TokenManager
import com.example.drugbank.common.constant.Constant
import com.example.drugbank.data.api.UserAPIService
import com.example.drugbank.data.model.User
import com.example.drugbank.databinding.FragmentSavedBinding
import com.example.drugbank.repository.UserAPIRepository
import com.example.drugbank.respone.UserListResponse
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@AndroidEntryPoint
class SavedFragment : Fragment() {

   // private lateinit var binding:ActivityMainBinding
    private lateinit var _binding: FragmentSavedBinding

    @Inject
    lateinit var userAPIRepository: UserAPIRepository

    @Inject
    lateinit var userAdapterInject: UserAdapterInject
    lateinit var viewModel: SavedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(SavedViewModel::class.java)

        return _binding.root
    }

    override fun onResume() {
        super.onResume()
       // setUpListt()
        setUpComboBox()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tokenManager = TokenManager(requireContext())

        RetrofitClient.instance_User.getPageableUser(
            "Bearer ${tokenManager.getAccessToken()}",
            gender = 1

        )
            .enqueue(object : Callback<UserListResponse> {
            override fun onResponse(call: Call<UserListResponse>, response: Response<UserListResponse>) {
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
                    val UserAdapter = UserAdapter(userList)
                    _binding.rclListUser.adapter = UserAdapter

                } else {
                    println("Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                println("Failed to make API call: ${t.message}")
            }
        })
    }

    private fun setUpListt() {
        //val UserAdapter = UserAdapter(Constant.getUserList())
       // _binding.rclListUser.adapter = UserAdapter
    }

    private fun setUpComboBox() {
        val rolelist = resources.getStringArray(R.array.RoleName)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu, rolelist)
        _binding.atcRoleListCombo.setAdapter(arrayAdapter)

        val genderList = resources.getStringArray(R.array.Gender)
        val arrayApderGender = ArrayAdapter(requireContext(), R.layout.dropdown_menu, genderList)
        _binding.atcGenderList.setAdapter(arrayApderGender)
    }




}