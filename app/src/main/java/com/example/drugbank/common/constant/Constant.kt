package com.example.drugbank.common.constant

import com.example.drugbank.R
import com.example.drugbank.ui.drugstore.DrugStoreFragment

class Constant {
    companion object {
        const val SEARCH_NAV_ID = 0;
        const val SAVED_NAV_ID = 1;
        const val RECORD_NAV_ID = 2;
        const val DRUGSTORE_NAV_ID = 3;
        const val SETTING_NAV_ID = 4;


        fun getNavSeleted(selected: Int): Int {
            val myHashMap = HashMap<Int, Int>()
            myHashMap[SEARCH_NAV_ID] = R.id.searchFragmentNav
            myHashMap[SAVED_NAV_ID] = R.id.savedFragmentNav
            myHashMap[RECORD_NAV_ID] = R.id.recordFragmentnav
            myHashMap[DRUGSTORE_NAV_ID] = R.id.drugStoreFragmentNav
            myHashMap[SETTING_NAV_ID] = R.id.settingFragmentNav

            return myHashMap[selected] ?: -1
        }
    }

}