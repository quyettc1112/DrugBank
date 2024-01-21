package com.example.drugbank.ui.activity.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.drugbank.common.BaseAPI.RetrofitClient
import com.example.drugbank.data.model.LoginDAO
import com.example.drugbank.data.model.Token
import com.example.drugbank.databinding.ActivityLoginBinding
import com.example.drugbank.ui.activity.auth.register.RegisterActivity
import com.example.drugbank.ui.activity.main.MainActivity
import com.example.healthcarecomp.base.BaseActivity
import com.google.common.base.Strings
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private lateinit var _binding: ActivityLoginBinding
    private lateinit var _Token: Token
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
         setContentView(_binding.root)

        _binding.btnSignUp.setOnClickListener {
            onSignUpClick()
        }
        _binding.btnCheckSecure.setOnClickListener {
            onCheckSecureClick()
        }
        //onSignUpClcik()

        openRegisterActivity()
        onBackLickIcon()
        onBackHandle()
    }

    private fun onSignUpClick() {
        val userName = _binding.etUsername.text.toString()
        val password = _binding.etPassword.text.toString()

        if (!Strings.isNullOrEmpty(userName) && !Strings.isNullOrEmpty(password)){
            val loginDAO = LoginDAO(userName, password);
            RetrofitClient.instance.login(loginDAO).enqueue(object : Callback<Token> {
                override fun onResponse(call: Call<Token>, response: Response<Token>) {
                    if (response.isSuccessful) {
                        val tokenData = response.body()
                        if (tokenData != null) {
                            val accessToken = tokenData.accessToken
                            val refreshToken = tokenData.refreshToken
                            _Token = Token(accessToken, refreshToken)

                            Toast.makeText(this@LoginActivity, _Token.accessToken, Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@LoginActivity, "Không có gì cả", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Xử lý lỗi
                    }
                }
                override fun onFailure(call: Call<Token>, t: Throwable) {
                    Log.d("Bug", t.toString())
                }
            }
            )
        } else {
            Toast.makeText(this@LoginActivity, "Null Input", Toast.LENGTH_LONG).show()
        }
    }

    private fun onCheckSecureClick() {
        RetrofitClient.instance_Test.getSecureData("Bearer ${_Token.accessToken}").enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    // Xử lý dữ liệu phản hồi kiểu String ở đây
                    if (responseData != null) {
                        Toast.makeText(this@LoginActivity, responseData.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                   Toast.makeText(this@LoginActivity, response.message(), Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("Bug", t.toString())
            }
        })

    }



    private fun onBackLickIcon() {
        _binding.customToolbar.onStartIconClick = {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun openRegisterActivity() {
        _binding.btnRegister.setOnClickListener {
            // Toast.makeText(this, "Check Click", Toast.LENGTH_SHORT).show()
            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
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

    // Xử lý sự kiện onback




}