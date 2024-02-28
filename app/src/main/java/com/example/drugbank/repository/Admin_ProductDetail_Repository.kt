package com.example.drugbank.repository

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
}