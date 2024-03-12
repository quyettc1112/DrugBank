package com.example.drugbank.repository

import com.example.drugbank.respone.ProductListRespone
import com.example.drugbank.respone.UserListResponse
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import okhttp3.MultipartBody

import retrofit2.http.Query


interface API_User_Service {

    @Multipart
    @POST("api/storage/user")
    fun uploadImage(
        @Header("Authorization") authorization: String,
        @Query("email") email: String?,
        @Part image: MultipartBody.Part?
    ): Call<UserListResponse.User?>


    @Multipart
    @POST("api/storage/approval-products")
    fun uploadImageProduct(
        @Header("Authorization") authorization: String,
        @Query("ApprovalProductID") approvalProductID: Int?,
        @Part image: MultipartBody.Part?
    ):  Call<ProductListRespone.Content?>


}