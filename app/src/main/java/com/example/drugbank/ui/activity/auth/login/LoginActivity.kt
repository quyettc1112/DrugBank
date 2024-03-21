package com.example.drugbank.ui.activity.auth.login

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.drugbank.R
import com.example.drugbank.base.dialog.ConfirmDialog
import com.example.drugbank.base.dialog.ErrorDialog
import com.example.drugbank.common.BaseAPI.RetrofitClient
import com.example.drugbank.common.Token.TokenManager
import com.example.drugbank.common.constant.Constant
import com.example.drugbank.data.dto.UpdateUserRequestDTO
import com.example.drugbank.data.model.LoginDTO
import com.example.drugbank.data.model.Token
import com.example.drugbank.databinding.ActivityLoginBinding
import com.example.drugbank.repository.API_User_Repository
import com.example.drugbank.repository.Admin_UserM_Repository
import com.example.drugbank.respone.UserListResponse
import com.example.drugbank.ui.activity.auth.register.RegisterActivity
import com.example.drugbank.ui.activity.main.MainActivity
import com.example.healthcarecomp.base.BaseActivity
import com.google.common.base.Strings
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private lateinit var _binding: ActivityLoginBinding
    private lateinit var _Token: Token

    @Inject
    lateinit var adminUser_Repository: Admin_UserM_Repository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val notificationMessage = intent.getStringExtra(Constant.FAILED_AUTHEN_BIOMRETIC)
        if (notificationMessage == null) {
            val userName = Constant.getSavedUsername(this@LoginActivity)
            val password = Constant.getSavedPassword(this@LoginActivity)

            // check value in share preference
            if (!userName.isNullOrEmpty() && !password.isNullOrEmpty()) {
                val loginDTO = LoginDTO(userName, password)
                Toast.makeText(this@LoginActivity, "${userName}, ${password}", Toast.LENGTH_SHORT).show()
                CallLogin(loginDTO)
            }
        }

        _binding = ActivityLoginBinding.inflate(layoutInflater)
         setContentView(_binding.root)

        _binding.btnSignUp.setOnClickListener {
            onSignUpClick()
        }

        setUpCheckRemember()
    }

    private fun onSignUpClick() {
        val userName = _binding.etUsername.text.toString()
        val password = _binding.etPassword.text.toString()

        if (!Strings.isNullOrEmpty(userName) && !Strings.isNullOrEmpty(password)){
            val loginDTO = LoginDTO(userName, password);
            CallLogin(loginDTO)
        } else {
            Toast.makeText(this@LoginActivity, "Null Input", Toast.LENGTH_LONG).show()
        }
    }

    private fun CallLogin(loginDTO: LoginDTO) {
        RetrofitClient.instance.login(loginDTO).enqueue(object : Callback<Token> {
            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                if (response.isSuccessful) {
                    val tokenData = response.body()
                    if (tokenData != null) {
                        val accessToken = tokenData.accessToken
                        val refreshToken = tokenData.refreshToken
                        _Token = Token(accessToken, refreshToken)
                        val tokenManager = TokenManager(this@LoginActivity)
                        tokenManager.saveAccessToken(_Token.accessToken)

                        CallGetUserByEmail(tokenManager, loginDTO.email)
                        if (_binding.checkBox.isChecked) {
                            savesSharePreference(loginDTO, tokenManager.getAccessToken().toString())
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Không có gì cả", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                if (response.code() == 404) {
                    val error = ErrorDialog(
                        context = this@LoginActivity,
                        errorContent = response.errorBody()!!.string(),
                        textButton = "Back"
                    )
                    error.show()
                }
                if (response.code() == 401) {
                    val error = ErrorDialog(
                        context = this@LoginActivity,
                        errorContent = "Account Banned",
                        textButton = "Back"
                    )
                    error.show()
                }

                if (response.code() == 403) {
                    val error = ErrorDialog(
                        context = this@LoginActivity,
                        errorContent = "Wrong Email or Password",
                        textButton = "Back"
                    )
                    error.show()
                }
            }

            override fun onFailure(call: Call<Token>, t: Throwable) {
                Toast.makeText(this@LoginActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        )
    }

    private fun CallGetUserByEmail(token: TokenManager, currentEmailUser: String) {
        adminUser_Repository.getUserByEmail(
            authorization = "Bearer ${token.getAccessToken()}",
            email = currentEmailUser.toString()
        ).enqueue(object : retrofit2.Callback<UserListResponse.User> {
            override fun onResponse(
                call: Call<UserListResponse.User>,
                response: Response<UserListResponse.User>
            ) {
                if (response.isSuccessful) {
                    Log.d("What", "true")
                    val userRespone: UserListResponse.User? = response.body()
                    Constant.saveCurrentUser(this@LoginActivity, userRespone!!)
                    IntentToHomePage()
                }
                else {
                    Log.d("What", "No")
                    val errorDialog = ErrorDialog(this@LoginActivity, errorContent =  response.code().toString(), textButton = "Back")
                    errorDialog.show()
                }
            }

            override fun onFailure(call: Call<UserListResponse.User>, t: Throwable) {
                val errorDialog = ErrorDialog(this@LoginActivity, errorContent =  t.message.toString(), textButton = "Back")
                errorDialog.show()
            }
        })
    }

    private fun IntentToHomePage() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()

    }


    private fun onBackHandle() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        this.onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun savesSharePreference(loginDTO: LoginDTO, token: String){
        val sharedPreferences = this.getSharedPreferences(Constant.USER_VALUE_SF, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(Constant.USERNAME_OR_EMAIL, loginDTO.email)
        editor.putString(Constant.USER_PASSWORD, loginDTO.password)
        editor.putString(Constant.USER_TOKEN, token)
        editor.apply()

    }


    private fun setUpCheckRemember(){
        _binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val confirmDialog = ConfirmDialog(
                    this@LoginActivity,
                    object : ConfirmDialog.ConfirmCallback {
                        override fun negativeAction() {
                            _binding.checkBox.isChecked = false
                        }
                        override fun positiveAction() {
                            _binding.checkBox.isChecked = true
                        }
                    },
                    title = "Confirm",
                    message = "We will confirm your identity through Biometric (Face recognition, fingerprint.....)",
                    positiveButtonTitle = "Yes",
                    negativeButtonTitle = "No"
                )
                confirmDialog.show()
                /*confirmDialog.window?.setBackgroundDrawable(ColorDrawable(this@LoginActivity.getColor(
                    R.color.zxing_transparent)))*/
                saveRememberMeState(true)
            } else {
                // Nếu người dùng không chọn "Remember Me", bạn có thể thực hiện các hành động ở đây, ví dụ:
                // Xóa trạng thái khỏi SharedPreferences
                saveRememberMeState(false)
            }
        }



    }

    private fun saveRememberMeState(isChecked: Boolean) {

    }




}