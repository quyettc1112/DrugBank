package com.example.drugbank.apiService

import com.example.drugbank.respone.ProductDetailRespone
import com.example.drugbank.respone.ProductListRespone
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface Admin_ProductM_APIService {
    @GET("admin/approval-products")
    fun getProductList(
        @Header("Authorization") authorization: String,
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Query("sortField") sortField: String,
        @Query("sortOrder") sortOrder: String,
        @Query("search") search: String,
        ): Call<ProductListRespone>


    @GET("admin/approval-products/FDA")
    fun getProductListFDA(
        @Header("Authorization") authorization: String,
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Query("sortField") sortField: String,
        @Query("sortOrder") sortOrder: String,
        @Query("search") search: String,
    ): Call<ProductListRespone>


    @GET("admin/approval-products/ANSM")
    fun getProductListANSM(
        @Header("Authorization") authorization: String,
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Query("sortField") sortField: String,
        @Query("sortOrder") sortOrder: String,
        @Query("search") search: String,
    ): Call<ProductListRespone>


    @GET("admin/approval-products/DAV")
    fun getProductListDAV(
        @Header("Authorization") authorization: String,
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Query("sortField") sortField: String,
        @Query("sortOrder") sortOrder: String,
        @Query("search") search: String,
    ): Call<ProductListRespone>

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

//    @POST("admin/approval-product-management/update-approval-products")
//    fun updateProduct(
//
//
//    )

}