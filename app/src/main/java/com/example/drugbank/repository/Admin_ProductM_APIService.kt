package com.example.drugbank.repository

import com.example.drugbank.respone.ProductDetailRespone
import com.example.drugbank.respone.ProductListRespone
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface Admin_ProductM_APIService {

    @GET("admin/approval-product-management/approval-products")
    fun getProductList(
        @Header("Authorization") authorization: String,
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Query("sortField") sortField: String,
        @Query("sortOrder") sortOrder: String,
        @Query("search") search: String,
        ): Call<ProductListRespone>


    @GET("admin/approval-product-management/approval-product-detail")
    fun getProductDetail(
        @Header("Authorization") authorization: String,
        @Query("id") id: Int,
    ): Call<ProductDetailRespone>

    @DELETE("admin/approval-product-management/delete-approval-product")
    fun deleteProduuct(
        @Header("Authorization") authorization: String,
        @Query("id") id: Int
    ): Call<Void>

//    @POST("admin/approval-product-management/update-approval-products")
//    fun updateProduct(
//
//
//    )

}