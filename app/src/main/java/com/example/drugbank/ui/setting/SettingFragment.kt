package com.example.drugbank.ui.setting

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.drugbank.R
import com.example.drugbank.common.Token.TokenManager
import com.example.drugbank.common.constant.Constant
import com.example.drugbank.data.model.User
import com.example.drugbank.databinding.FragmentSearchBinding
import com.example.drugbank.databinding.FragmentSettingBinding
import com.example.drugbank.repository.Admin_UserM_Repository
import com.example.drugbank.respone.UserListResponse
import com.example.drugbank.ui.activity.auth.login.LoginActivity
import com.example.drugbank.ui.activity.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

@AndroidEntryPoint
class SettingFragment : Fragment() {


    private lateinit var viewModel: SettingViewModel
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private lateinit var currentEmailUser: String
    private lateinit var tokenManager: TokenManager

    @Inject
    lateinit var userRepository: Admin_UserM_Repository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        logout()
        tokenManager = TokenManager(requireContext())
        currentEmailUser = Constant.getSavedUsername(requireContext()).toString()

        CallGetUserByEmail()

        val rootView = binding.root

        return rootView
    }

    private fun logout() {
        _binding!!.logout.setOnClickListener {
            Constant.removeAllSavedValues(requireContext())
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingViewModel::class.java)

//        val activity = requireActivity() as MainActivity
//        activity.showLoginDialog()
    }

    private fun CallGetUserByEmail() {
        userRepository.getUserByEmail(
            authorization = "Bearer ${tokenManager.getAccessToken()}",
            email = currentEmailUser
        ).enqueue(object : retrofit2.Callback<UserListResponse.User> {
            override fun onResponse(
                call: Call<UserListResponse.User>,
                response: Response<UserListResponse.User>
            ) {
                if (response.isSuccessful) {

                    Log.d("CheckUser", response.body().toString())
                }
                else {
                    Log.d("CheckUser", response.code().toString())
                }
            }

            override fun onFailure(call: Call<UserListResponse.User>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
    private fun bindDataUser() {


    }



}