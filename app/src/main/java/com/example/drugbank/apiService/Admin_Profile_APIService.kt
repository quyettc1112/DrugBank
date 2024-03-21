package com.example.drugbank.apiService

import com.example.drugbank.respone.ProfileDetailRespone
import com.example.drugbank.respone.ProfileListRespone
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface Admin_Profile_APIService {

    @GET("admin/profile-products")
    fun getProfileList(
        @Header("Authorization") authorization: String,
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Query("search") search: String
    ): Call<ProfileListRespone>

    @GET("admin/profile-products/{id}")
    fun getProfileDetail(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int,
    ): Call<ProfileDetailRespone>

}