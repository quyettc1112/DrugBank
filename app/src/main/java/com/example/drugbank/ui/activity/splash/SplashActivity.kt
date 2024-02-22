package com.example.drugbank.ui.activity.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.drugbank.R
import com.example.drugbank.common.constant.Constant
import com.example.drugbank.data.model.LoginDTO
import com.example.drugbank.ui.activity.auth.login.LoginActivity
import com.example.drugbank.ui.activity.main.MainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        loadLocalUserData()

    }

    private fun loadLocalUserData() {
        val  delayMillis: Long = 750 // 1,25  second
        Handler(Looper.getMainLooper()).postDelayed({
            openAuthActivity()
        }, delayMillis)
    }

    // Intent on Main Activity
    private fun openAuthActivity() {

        val userName = Constant.getSavedUsername(this@SplashActivity)
        val password = Constant.getSavedPassword(this@SplashActivity)

        // check value in share preference
        if (!userName.isNullOrEmpty() && !password.isNullOrEmpty()) {
            val loginDTO = LoginDTO(userName, password)
            Toast.makeText(this@SplashActivity, "${userName}, ${password}", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }



    }
}