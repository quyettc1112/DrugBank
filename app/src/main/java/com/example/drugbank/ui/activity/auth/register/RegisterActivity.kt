package com.example.drugbank.ui.activity.auth.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.drugbank.R
import com.example.drugbank.databinding.ActivityRegisterBinding
import com.example.drugbank.ui.activity.auth.login.LoginActivity
import com.example.drugbank.ui.activity.main.MainActivity
import com.example.healthcarecomp.base.BaseActivity

class RegisterActivity : BaseActivity() {

    private lateinit var _binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(_binding.root)


        onBackLickIcon()
    }

    private fun onBackLickIcon() {
        _binding.customToolbar.onStartIconClick = {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}