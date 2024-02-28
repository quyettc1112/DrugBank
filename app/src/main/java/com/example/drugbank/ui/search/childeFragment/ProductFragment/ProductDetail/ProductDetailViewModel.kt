package com.example.drugbank.ui.search.childeFragment.ProductFragment.ProductDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class ProductDetailViewModel @Inject constructor() : ViewModel() {

    private var _isLoading = MutableLiveData<Boolean>()

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    // Hàm này được gọi khi bạn muốn cập nhật trạng thái loading
    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }
}