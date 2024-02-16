package com.example.drugbank.ui.search.childeFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.drugbank.data.model.Drug
import javax.inject.Inject

class DrugViewModel @Inject constructor() : ViewModel() {

    val currentDrugList = MutableLiveData<List<Drug>?>()
    val selectedSortField = MutableLiveData<String>()
    val searchField = MutableLiveData<String>()
    val selectefSortBy = MutableLiveData<String>()
    val currentPage = MutableLiveData<Int>()

    init {
        selectedSortField.value = "id"
        selectefSortBy.value = "asc"
        searchField.value = ""
        currentPage.value = 0

    }

    fun loadMoreDruglist(list: List<Drug>) {
        val currentList = currentDrugList.value ?: emptyList()
        val updatedList = currentList + list
        currentDrugList.value = updatedList
    }

    fun incrementCurrentPage() {
        val currentValue = currentPage.value ?: 0 // Lấy giá trị hiện tại của currentPage, hoặc mặc định là 0 nếu chưa có giá trị
        currentPage.value = currentValue + 1 // Gán giá trị mới cho currentPage là giá trị hiện tại cộng thêm 1
    }

    fun resetCurrentPage() {
        currentPage.value = 0 // Gán giá trị mới cho currentPage là 0
    }

    fun emptyDrugList() {
        currentDrugList.value = null
    }
}