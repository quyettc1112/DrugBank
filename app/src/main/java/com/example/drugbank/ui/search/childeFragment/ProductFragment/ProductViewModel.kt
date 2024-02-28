package com.example.drugbank.ui.search.childeFragment.ProductFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.drugbank.respone.ProductListRespone
import javax.inject.Inject

class ProductViewModel @Inject constructor() : ViewModel()  {

    val currentProductList = MutableLiveData<List<ProductListRespone.Content>?>()
    val currentPage = MutableLiveData<Int>()

    val currentSorField = MutableLiveData<String>()
    val currentSortBy = MutableLiveData<String>()
    val currentSearchValue = MutableLiveData<String>()

    val totalElement = MutableLiveData<Int>()

    val current_ID_Item = MutableLiveData<Int>()

    init {
        currentPage.value = 0;
        currentSorField.value = "id"
        currentSortBy.value = "asc"
        currentSearchValue.value = ""
        current_ID_Item.value = 0
        totalElement.value = 0
    }

    fun loafMoreProductList(list: List<ProductListRespone.Content>) {
        val  currentList = currentProductList.value ?: emptyList()
        val updatedList = currentList + list
        currentProductList.value = updatedList
    }

    fun incrementCurrentPage() {
        val currentValue = currentPage.value ?: 0 // Lấy giá trị hiện tại của currentPage, hoặc mặc định là 0 nếu chưa có giá trị
        currentPage.value = currentValue + 1 // Gán giá trị mới cho currentPage là giá trị hiện tại cộng thêm 1
    }


    fun resetAllValue() {
        currentSearchValue.value = ""
        currentProductList.value = null
        totalElement.value = 0
        currentPage.value = 0 // Gán giá trị mới cho currentPage là 0
    }




}