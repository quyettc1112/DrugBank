package com.example.drugbank.common.Token

import android.content.Context
import android.content.SharedPreferences
import com.example.drugbank.common.constant.Constant

class TokenManager(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(Constant.TOKEN_USER, Context.MODE_PRIVATE)

    // Lưu accessToken vào SharedPreferences
    fun saveAccessToken(accessToken: String) {
        sharedPreferences.edit().putString(Constant.ACCESS_TOKEN, accessToken).apply()
    }

    // Lấy accessToken từ SharedPreferences
    fun getAccessToken(): String? {
        return sharedPreferences.getString(Constant.ACCESS_TOKEN, null)
    }

    // Xóa accessToken từ SharedPreferences
    fun clearAccessToken() {
        sharedPreferences.edit().remove(Constant.ACCESS_TOKEN).apply()
    }
}