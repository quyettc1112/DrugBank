package com.example.drugbank.ui.record.RecordDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.drugbank.data.model.Section
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random


class RecordDetailViewModel: ViewModel() {

    private var _isLoading = MutableLiveData<Boolean>()



    val isLoading: LiveData<Boolean>
        get() = _isLoading
    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }




}