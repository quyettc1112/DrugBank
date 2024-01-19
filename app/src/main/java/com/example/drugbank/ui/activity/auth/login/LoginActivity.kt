package com.example.drugbank.ui.activity.auth.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.drugbank.R
import com.example.drugbank.databinding.ActivityLoginBinding
import com.example.drugbank.databinding.ActivityMainBinding
import com.example.drugbank.ui.activity.auth.register.RegisterActivity
import com.example.drugbank.ui.activity.main.MainActivity
import com.example.healthcarecomp.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private lateinit var _binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
         setContentView(_binding.root)

        openRegisterActivity()
        onBackLickIcon()
        onBackHandle()
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