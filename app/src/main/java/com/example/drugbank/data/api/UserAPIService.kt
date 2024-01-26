package com.example.drugbank.data.api



import com.example.drugbank.data.model.User
import com.example.drugbank.respone.UserListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface UserAPIService {

    @GET("user/get-pageable-users")
    fun getPageableUser(
        @Header("Authorization") authorization: String,
        @Query("gender") gender: Int
    ): Call<UserListResponse>

}