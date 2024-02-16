package com.example.drugbank.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class SearchViewModel @Inject constructor(): ViewModel() {
    val currentSearchDrugData = MutableLiveData<String>()
    val currentSearchProductData = MutableLiveData<String>()

    init {
        currentSearchDrugData.value = ""
        currentSearchProductData.value =""
    }
}