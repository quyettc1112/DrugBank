package com.example.drugbank.ui.activity.splash

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.drugbank.R
import com.example.drugbank.base.dialog.ErrorDialog
import com.example.drugbank.common.BaseAPI.RetrofitClient
import com.example.drugbank.common.Token.TokenManager
import com.example.drugbank.common.constant.Constant
import com.example.drugbank.data.model.LoginDTO
import com.example.drugbank.data.model.Token
import com.example.drugbank.data.model.User
import com.example.drugbank.helper.BiometricHelper
import com.example.drugbank.ui.activity.auth.login.LoginActivity
import com.example.drugbank.ui.activity.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {

    private lateinit var _Token: Token

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private val requestCode: Int = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        loadLocalUserData()

//        val hasFaceBiometric = packageManager.hasSystemFeature(PackageManager.FEATURE_FACE)
//        displayMessage(hasFaceBiometric.toString())
//        if (hasFaceBiometric) {
//            checkCanUseBiometric()
//            checkFingerSpint()
//        }
        //checkBiometric()


    }

    private fun loadLocalUserData() {
        val  delayMillis: Long = 500 // 1,25  second
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
            CallLogin(loginDTO)

        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
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
                        val tokenManager = TokenManager(this@SplashActivity)
                        tokenManager.saveAccessToken(_Token.accessToken)
                        Constant.removeAllSavedValues(this@SplashActivity)
                        Constant.saveUserCredentials(this@SplashActivity, loginDTO.email, loginDTO.password, _Token.accessToken)
                        checkBiometric()
                    }
                }
                if (response.code() == 404) {
                    val error = ErrorDialog(
                        context = this@SplashActivity,
                        errorContent = response.errorBody()!!.string(),
                        textButton = "Back"
                    )
                    error.show()
                }
            }

            override fun onFailure(call: Call<Token>, t: Throwable) {
                Toast.makeText(this@SplashActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        )
    }




    private fun checkBiometric() {
        val biometricManager = BiometricManager.from(this)

        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                displayMessage("Biometric authentication is available")

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                displayMessage("This device doesn't support biometric authentication")

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                displayMessage("Biometric authentication is currently unavailable")

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL)
                if (enrollIntent.resolveActivity(this@SplashActivity.packageManager) != null) {
                    enrollIntent.putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
                    )
                    startActivityForResult(enrollIntent, requestCode)
                } else {
                        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED
                }
                /*displayMessage("Biometric authentication is currently unavailable")
                val intent = Intent(Settings.ACTION_BIOMETRIC_ENROLL)
                startActivityForResult(intent, CONTEXT_INCLUDE_CODE)*/
            }

        }

        val executor = ContextCompat.getMainExecutor(this)
        val intent = Intent(this, MainActivity::class.java)
        val intentLogin = Intent(this, LoginActivity::class.java)
        biometricPrompt =
            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    displayMessage("Authentication error: $errString")
                    intentLogin.putExtra(Constant.FAILED_AUTHEN_BIOMRETIC, Constant.FAILED_AUTHEN_BIOMRETIC)
                    startActivity(intentLogin)
                    finish()
                }
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    displayMessage("Authentication succeeded!")
                    startActivity(intent)
                    finish()

                }
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    displayMessage("Authentication failed")
                    intentLogin.putExtra(Constant.FAILED_AUTHEN_BIOMRETIC, Constant.FAILED_AUTHEN_BIOMRETIC)
                    startActivity(intentLogin)
                    finish()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authen with Biometric")
            .setDescription("Position your face within the frame.")
            .setConfirmationRequired(true)
            //.setNegativeButtonText("Cancel")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK
                    or BiometricManager.Authenticators.BIOMETRIC_STRONG
                    or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            .setSubtitle("Check Authentication")
            .build()

        biometricPrompt.authenticate(promptInfo)

    }

    private fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }


}