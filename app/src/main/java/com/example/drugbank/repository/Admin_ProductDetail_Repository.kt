package com.example.drugbank.repository

import com.example.drugbank.apiService.Admin_ProductDetail_Service
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class Admin_ProductDetail_Repository @Inject constructor (
    private val admin_productDetail_service: Admin_ProductDetail_Service
){
    fun getProductDetail(
        authorization: String,
        id: Int
    ) = admin_productDetail_service.getProductDetail(authorization, id)



    fun deleleProduct(
        authorization: String,
        id: Int
    )  = admin_productDetail_service.deleteProduuct(authorization, id)
}