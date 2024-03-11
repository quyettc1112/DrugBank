package com.example.drugbank.ui.activity.main

import androidx.lifecycle.MutableLiveData
import com.example.healthcarecomp.base.BaseViewModel

class MainViewModel: BaseViewModel() {

    var currentNav = MutableLiveData<Int?>()
    var testValue = MutableLiveData<Int>()

    init {
        testValue.value = 0

    }
    fun ChangeNav(IDNav: Int) {
        currentNav.value = IDNav
    }
    fun setUpNav() {
        currentNav.value = 0
    }




}