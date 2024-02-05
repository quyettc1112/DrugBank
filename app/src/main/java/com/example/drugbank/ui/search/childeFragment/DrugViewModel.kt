package com.example.drugbank.ui.search.childeFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.drugbank.data.model.Drug
import javax.inject.Inject

class DrugViewModel @Inject constructor() : ViewModel() {

    val currentDrugList = MutableLiveData<List<Drug>>()

    fun loadMoreDruglist(list: List<Drug>) {
        val currentList = currentDrugList.value ?: emptyList()
        val updatedList = currentList + list
        currentDrugList.value = updatedList
    }
}