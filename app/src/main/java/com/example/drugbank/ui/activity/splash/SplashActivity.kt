package com.example.drugbank.ui.activity.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.drugbank.R
import com.example.drugbank.ui.activity.main.MainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        loadLocalUserData()

    }

    private fun loadLocalUserData() {
        val  delayMillis: Long = 1250 // 1,25  second
        Handler(Looper.getMainLooper()).postDelayed({
            openAuthActivity()
        }, delayMillis)
    }

    // Intent on Main Activity
    private fun openAuthActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}