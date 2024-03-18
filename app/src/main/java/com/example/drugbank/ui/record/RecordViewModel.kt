package com.example.drugbank.ui.record

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.drugbank.respone.ProductListRespone
import com.example.drugbank.respone.ProfileListRespone
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor()  : ViewModel() {

    val currentProfileList = MutableLiveData<List<ProfileListRespone.Content>?>()
    val currentPage = MutableLiveData<Int>()
    private var _isLoading = MutableLiveData<Boolean>()
    val currentSearchValue = MutableLiveData<String>()
    val totalElement = MutableLiveData<Int>()

    val current_ID_Item = MutableLiveData<Int>()

    val isLoading: LiveData<Boolean>
        get() = _isLoading
    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }
    init {
        totalElement.value = 0
        currentSearchValue.value = ""
        currentPage.value = 0

    }
    fun loadMoreRecord(list: List<ProfileListRespone.Content>) {
        val  currentList = currentProfileList.value ?: emptyList()
        val updatedList = currentList + list
        currentProfileList.value = updatedList
    }
    fun resetAllValue() {
        currentProfileList.value = emptyList()
    }
    fun incrementCurrentPage() {
        val currentValue = currentPage.value ?: 0 // Lấy giá trị hiện tại của currentPage, hoặc mặc định là 0 nếu chưa có giá trị
        currentPage.value = currentValue + 1 // Gán giá trị mới cho currentPage là giá trị hiện tại cộng thêm 1
    }
    fun resetSearchValue() {
        currentSearchValue.value = ""
    }

}