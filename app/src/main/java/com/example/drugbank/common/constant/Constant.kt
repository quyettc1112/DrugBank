package com.example.drugbank.common.constant

import android.content.Context
import com.example.drugbank.R
import com.example.drugbank.data.model.Drug
import com.example.drugbank.respone.ProductDetailRespone
import com.example.drugbank.respone.ProductListRespone
import com.example.drugbank.respone.UserListResponse
import com.example.drugbank.ui.drugstore.DrugStoreFragment
import org.checkerframework.checker.units.qual.Current
import org.json.JSONObject

class Constant {
    companion object {
        const val USER_VALUE_SF = "USER_VALUE_SF"
        const val USERNAME_OR_EMAIL = "USERNAME_OR_EMAIL"
        const val USER_PASSWORD = "USER_PASSWORD"
        const val USER_TOKEN = "USER_TOKEN"

        const val SEARCH_NAV_ID = 0;
        const val USER_MANAGER_NAV_ID = 3;
        const val RECORD_NAV_ID = 2;
        const val DRUGSTORE_NAV_ID = 1;
        const val SETTING_NAV_ID = 4;
        const val PRODUCT_DETAIL = 5
        const val LOGIN_NAV_ID = 99;
        const val DEFAULT_ERROR_MESSAGE: String = "An error occurred"
        const val DOCTOR_SECURITY_DOCTOR = "123"
        const val TOKEN_USER = "TOKEN_USER"
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val REFESH_TOKEN = "REFESH_TOKEN"

        const val CURRENT_USER = "CURRENT_USER"
        const val CURRENT_USER_VALUE = "CURRENT_USER_VALUE"


        const val CURRENT_FDA = "CURRENT_FDA"
        const val CURRENT_FDA_VALUE = "CURRENT_FDA_VALUE"


        const val CURRENT_PRODUCT_ID = "CURRENT_PRODUCT_ID"
        const val CURRENT_PRODUCT_ID_VALUE = "CURRENT_PRODUCT_ID_VALUE"
        const val CURRENT_PRODUCT_ID_VALUE_FIRST = "CURRENT_PRODUCT_ID_VALUE_FIRST"

        const val FAILED_AUTHEN_BIOMRETIC = "FAILED_AUTHEN_BIOMRETIC"

        fun getNavSeleted(selected: Int): Int {
            val myHashMap = HashMap<Int, Int>()
            myHashMap[SEARCH_NAV_ID] = R.id.searchFragmentNav
            myHashMap[USER_MANAGER_NAV_ID] = R.id.user_managerFragmentNav
            myHashMap[RECORD_NAV_ID] = R.id.recordFragmentnav
            myHashMap[DRUGSTORE_NAV_ID] = R.id.drugStoreFragmentNav
            myHashMap[SETTING_NAV_ID] = R.id.settingFragmentNav
            myHashMap[LOGIN_NAV_ID] = R.id.loginFragmentNav
            myHashMap[PRODUCT_DETAIL] = R.id.productDetailFragmentNav
            return myHashMap[selected] ?: -1
        }

        fun getSavedUsername(context: Context): String? {
            val sharedPreferences = context.getSharedPreferences(USER_VALUE_SF, Context.MODE_PRIVATE)
            return sharedPreferences.getString(USERNAME_OR_EMAIL, null)
        }

        fun saveCurrentUser(context: Context, user: UserListResponse.User) {
            val sharedPreferences = context.getSharedPreferences(CURRENT_USER, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            val userJson = JSONObject()
            userJson.put("id", user.id)
            userJson.put("username", user.username)
            userJson.put("email", user.email)
            userJson.put("fullname", user.fullname)
            userJson.put("dayOfBirth", user.dayOfBirth)
            userJson.put("gender", user.gender)
            userJson.put("roleName", user.roleName)
            userJson.put("isActive", user.isActive)
            if (user.avatar!= null) {
                userJson.put("avatar", user.avatar)
            } else  userJson.put("avatar", R.drawable.user_general)
            editor.putString(CURRENT_USER_VALUE, userJson.toString())
            editor.apply()
        }

        fun getCurrentUser(context: Context): UserListResponse.User? {
            val sharedPreferences = context.getSharedPreferences(CURRENT_USER, Context.MODE_PRIVATE)
            val userJsonString = sharedPreferences.getString(CURRENT_USER_VALUE, null)

            return if (userJsonString != null) {
                val userJson = JSONObject(userJsonString)
                UserListResponse.User(
                    userJson.getInt("id"),
                    userJson.getString("username"),
                    userJson.getString("email"),
                    userJson.getString("fullname"),
                    userJson.getString("dayOfBirth"),
                    userJson.getInt("gender"),
                    userJson.getString("roleName"),
                    userJson.getString("isActive"),
                    userJson.getString("avatar"),
                )
            } else {
                null
            }
        }
        fun getSavedPassword(context: Context): String? {
            val sharedPreferences = context.getSharedPreferences(USER_VALUE_SF, Context.MODE_PRIVATE)
            return sharedPreferences.getString(USER_PASSWORD, null)
        }
        fun removeAllSavedValues(context: Context) {
            val sharedPreferences = context.getSharedPreferences(USER_VALUE_SF, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            sharedPreferences.all.keys.forEach { key ->
                editor.remove(key)
            }
            editor.apply()
        }

        fun removeAllCurrentUser(context: Context) {
            val sharedPreferences = context.getSharedPreferences(CURRENT_USER, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            sharedPreferences.all.keys.forEach { key ->
                editor.remove(key)
            }
            editor.apply()
        }

        fun saveUserCredentials(context: Context, username: String?, password: String?, token: String?) {
            val sharedPreferences = context.getSharedPreferences(USER_VALUE_SF, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(USERNAME_OR_EMAIL, username)
            editor.putString(USER_PASSWORD, password)
            editor.putString(USER_TOKEN, token)

            editor.apply()
        }

        fun getDrugList(): ArrayList<String> {
            val list = ArrayList<String>()

           list.add("a")
           list.add("a")
           list.add("a")
           list.add("a")
           list.add("a")

            return list
        }



    }




}