package com.example.drugbank.ui.saved

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drugbank.common.Resource.Resource
import com.example.drugbank.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class SavedViewModel @Inject constructor() : ViewModel() {

    val selectedRole = MutableLiveData<String>()
    val selectedGender = MutableLiveData<Int?>()
    val selectedActive = MutableLiveData<String?>()

    val userList = MutableLiveData<List<User>>()

    init {
        selectedRole.value = "USER"
        selectedGender.value = null
        selectedActive.value = null
    }

    fun getUserList(userListCurrent: List<User>) = viewModelScope.launch{
        userList.postValue(userListCurrent)
    }




}