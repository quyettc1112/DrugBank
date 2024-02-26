package com.example.drugbank.ui.usermanager

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drugbank.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject
@HiltViewModel
class UserManagerViewModel @Inject constructor() : ViewModel() {

    private var calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

     var user_count= MutableLiveData<Int>()
     var user_active= MutableLiveData<Int>()
     var user_gender= MutableLiveData<Int>()



    val selectedRole = MutableLiveData<String>()
    val selectedGender = MutableLiveData<Int?>()
    val selectedActive = MutableLiveData<String?>()

    val userList = MutableLiveData<List<User>>()


    val dob = MutableLiveData<String>()

    init {
        selectedRole.value = "USER"
        selectedGender.value = null
        selectedActive.value = null
        dob.value = dateFormat.format(calendar.time)

        user_count.value = 0
        user_active.value = 0
        user_gender.value = 0


    }

    fun getUserList(userListCurrent: List<User>) = viewModelScope.launch{
        userList.postValue(userListCurrent)
    }

    fun updateDOBValue(string: String) {
        dob.value = string

    }






}