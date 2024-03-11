package com.example.drugbank.ui.activity.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.drugbank.R
import com.example.drugbank.common.Token.TokenManager
import com.example.drugbank.common.constant.Constant
import com.example.drugbank.databinding.ActivityMainBinding
import com.example.drugbank.databinding.DialogConfirmBinding
import com.example.drugbank.repository.Admin_UserM_Repository
import com.example.drugbank.respone.UserListResponse
import com.example.healthcarecomp.base.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import me.ibrahimsn.lib.NiceBottomBar
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding:ActivityMainBinding
    private lateinit var _mainViewModel: MainViewModel

    private  var currentEmailUser: String? = null
    private lateinit var tokenManager: TokenManager

    @Inject
    lateinit var userRepository: Admin_UserM_Repository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tokenManager = TokenManager(this@MainActivity)
        currentEmailUser = Constant.getSavedUsername(this@MainActivity)
        CallGetUserByEmail()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpBottomNav()

    }

    private fun setUpBottomNav() {
        _mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        _mainViewModel.setUpNav()
        _mainViewModel.currentNav.observe(this, Observer { currentId ->
            if (currentId != null) {
                val navController = findNavController(R.id.nav_host_fragment_activity_main)
                navController.navigate(Constant.getNavSeleted(currentId))
            }
        })
        binding.bottomBar.let { bt ->
            // Todo Check người dùng đã đăng nhập chưa ở dây
            bt.onItemSelected = {
                    _mainViewModel.ChangeNav(it)
            }
            bt.setBadge(2)
        }
    }

    fun showLoginDialog() {
        showLoginDialog(this, this, 0)
    }

    private fun CallGetUserByEmail() {
        userRepository.getUserByEmail(
            authorization = "Bearer ${tokenManager.getAccessToken()}",
            email = currentEmailUser.toString()
        ).enqueue(object : retrofit2.Callback<UserListResponse.User> {
            override fun onResponse(
                call: Call<UserListResponse.User>,
                response: Response<UserListResponse.User>
            ) {
                if (response.isSuccessful) {
                    val userRespone: UserListResponse.User? = response.body()
                    Log.d("CheckUser", userRespone.toString())

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


}