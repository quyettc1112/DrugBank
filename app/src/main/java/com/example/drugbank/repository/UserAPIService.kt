package com.example.drugbank.repository



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
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Query("sortField") sortField: String,
        @Query("sortOrder") sortOrder: String,
        @Query("roleName") roleName: String?,
        @Query("status") status: String?,
        @Query("gender") gender: Int?,
    ): Call<UserListResponse>

}