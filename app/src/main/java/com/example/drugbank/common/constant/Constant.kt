package com.example.drugbank.common.constant

import android.content.Context
import com.example.drugbank.R
import com.example.drugbank.ui.drugstore.DrugStoreFragment

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
        const val LOGIN_NAV_ID = 99;
        const val DEFAULT_ERROR_MESSAGE: String = "An error occurred"
        const val DOCTOR_SECURITY_DOCTOR = "123"
        const val TOKEN_USER = "TOKEN_USER"
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val REFESH_TOKEN = "REFESH_TOKEN"

        fun getNavSeleted(selected: Int): Int {
            val myHashMap = HashMap<Int, Int>()
            myHashMap[SEARCH_NAV_ID] = R.id.searchFragmentNav
            myHashMap[USER_MANAGER_NAV_ID] = R.id.user_managerFragmentNav
            myHashMap[RECORD_NAV_ID] = R.id.recordFragmentnav
            myHashMap[DRUGSTORE_NAV_ID] = R.id.drugStoreFragmentNav
            myHashMap[SETTING_NAV_ID] = R.id.settingFragmentNav
            myHashMap[LOGIN_NAV_ID] = R.id.loginFragmentNav
            return myHashMap[selected] ?: -1
        }

        fun getSavedUsername(context: Context): String? {
            val sharedPreferences = context.getSharedPreferences(USER_VALUE_SF, Context.MODE_PRIVATE)
            return sharedPreferences.getString(USERNAME_OR_EMAIL, null)
        }

        fun getSavedPassword(context: Context): String? {
            val sharedPreferences = context.getSharedPreferences(USER_VALUE_SF, Context.MODE_PRIVATE)
            return sharedPreferences.getString(USER_PASSWORD, null)
        }



    }




}