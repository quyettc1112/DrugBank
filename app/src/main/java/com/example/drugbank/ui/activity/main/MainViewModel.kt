package com.example.drugbank.ui.activity.main

import androidx.lifecycle.MutableLiveData
import com.example.healthcarecomp.base.BaseViewModel

class MainViewModel: BaseViewModel() {

    var currentNav = MutableLiveData<Int?>()
    
    fun ChangeNav(IDNav: Int) {
        currentNav.value = IDNav
    }
    fun setUpNav() {
        currentNav.value = 0
    }




}