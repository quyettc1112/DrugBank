package com.example.drugbank.repository

import dagger.hilt.android.scopes.ActivityScoped
import java.net.IDN
import javax.inject.Inject

@ActivityScoped
class Admin_ProductM_Repository @Inject constructor(
    private val adminProductmRepository: Admin_ProductM_APIService
) {

    fun getProductList(
        authorization: String,
        pageNo: Int,
        pageSize: Int,
        sortField: String,
        sortOrder: String,
        search: String
    ) = adminProductmRepository.getProductList(authorization, pageNo, pageSize, sortField, sortOrder, search)


    fun getProductDetail(
        authorization: String,
        id: Int
    ) = adminProductmRepository.getProductDetail(authorization, id)

    fun deleteProduct(
        authorization: String,
        id: Int
    )  =adminProductmRepository.deleteProduuct(authorization, id)


}