package com.example.drugbank.ui.saved

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drugbank.common.Resource.Resource
import com.example.drugbank.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject
@HiltViewModel
class SavedViewModel @Inject constructor() : ViewModel() {

    private var calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("MMM dd yyyy")

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
    }

    fun getUserList(userListCurrent: List<User>) = viewModelScope.launch{
        userList.postValue(userListCurrent)
    }

    fun updateDOBValue(calendar: Calendar) {
        dob.value = dateFormat.format(calendar.time)

    }






}