package com.example.drugbank.ui.search.childeFragment.ProductFragment.ProductDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.drugbank.respone.ProductDetailRespone
import javax.inject.Inject

class ProductDetailViewModel @Inject constructor() : ViewModel() {

    private var _isLoading = MutableLiveData<Boolean>()

    val currentDetail = MutableLiveData<ProductDetailRespone?>()

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    // Hàm này được gọi khi bạn muốn cập nhật trạng thái loading
    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    init {
        currentDetail.value = null
    }

    fun setCurrentProductDetail(productDetailRespone: ProductDetailRespone) {
        currentDetail.value = productDetailRespone
    }

    fun resetCurrentProductDetail() {
        currentDetail.value = null

    }
}