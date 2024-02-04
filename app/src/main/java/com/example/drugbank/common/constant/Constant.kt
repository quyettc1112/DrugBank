package com.example.drugbank.common.constant

import com.example.drugbank.R
import com.example.drugbank.ui.drugstore.DrugStoreFragment

class Constant {
    companion object {
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
        const val REFESH_TOEKN = "REFESH_TOKEN"

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
        fun getUserList():  ArrayList<tempUser>{
            val itemList = ArrayList<tempUser>()
            var user1 =tempUser(R.drawable.anh_2, "Trần Cương Quyết")
            var user2 =tempUser(R.drawable.anh_3, "Trần Cương Quyết")
            var user3 =tempUser(R.drawable.anh_4, "Trần Cương Quyết")
            var user4 =tempUser(R.drawable.avatar_1, "Trần Cương Quyết")
            itemList.add(user1)
            itemList.add(user2)
            itemList.add(user3)
            //itemList.add(user4)
            return itemList
        }
    }

    class  tempUser(
        val imageIcon: Int,
        val nameIcon: String,
        val actionId:Int? = null

    )
    sealed class MEDICAL() {
        enum class INT(val range: IntRange, val dimension: String){
            HEARTH_RATE(40..200, "bpm"),
            BLOOD_SUGAR(40..200, "mg/dL")
        }
        enum class FLOAT(val range: ClosedFloatingPointRange<Float>, val dimension: String){
            WEIGHT(0f..300f, "kg"),
            HEIGHT(0f..300f, "cm"),
            BODY_TEMPERATURE(34f..40f, "C")
        }

        enum class STRING(val regex: Regex, val dimension: String){
            BLOOD_PRESSURE("""^\d{2,3}/\d{2,3}$""".toRegex(), "mg Hg")
        }

    }

}