package com.example.drugbank.ui.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor() : ViewModel() {

    private var calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val dob = MutableLiveData<String>()
    init {

        dob.value = dateFormat.format(calendar.time)



    }
    fun updateDOBValue(string: String) {
        dob.value = string

    }
}