package com.example.drugbank.ui.activity.main

import androidx.lifecycle.MutableLiveData
import com.example.drugbank.respone.UserListResponse
import com.example.healthcarecomp.base.BaseViewModel

class MainViewModel: BaseViewModel() {

    var currentNav = MutableLiveData<Int?>()
    var testValue = MutableLiveData<Int>()
    var currentUser = MutableLiveData<UserListResponse.User?>()

    init {
        testValue.value = 0
        currentUser.value =  null

    }
    fun ChangeNav(IDNav: Int) {
        currentNav.value = IDNav
    }
    fun setUpNav() {
        currentNav.value = 0
    }

    fun updateCurrentUser(user: UserListResponse.User?) {
        currentUser.value = user
    }

    fun clearCurrentUser() {
        currentUser.value = null
    }




}