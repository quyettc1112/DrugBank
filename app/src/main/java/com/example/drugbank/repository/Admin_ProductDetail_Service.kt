package com.example.drugbank.repository

import com.example.drugbank.respone.ProductDetailRespone
import dagger.hilt.android.scopes.ActivityScoped
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface Admin_ProductDetail_Service {
    @GET("admin/approval-product-detail")
    fun getProductDetail(
        @Header("Authorization") authorization: String,
        @Query("id") id: Int,
    ): Call<ProductDetailRespone>

    @DELETE("admin/approval-product/delete")
    fun deleteProduuct(
        @Header("Authorization") authorization: String,
        @Query("id") id: Int
    ): Call<Void>
}