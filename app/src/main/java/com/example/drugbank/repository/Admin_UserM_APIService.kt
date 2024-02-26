package com.example.drugbank.repository



import com.example.drugbank.data.dto.AddUserRequestDTO
import com.example.drugbank.data.dto.UpdateUserRequestDTO
import com.example.drugbank.respone.UserListResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface Admin_UserM_APIService {
    @GET("admin/user-management/users")
    fun getPageableUser(
        @Header("Authorization") authorization: String,
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Query("sortField") sortField: String,
        @Query("sortOrder") sortOrder: String,
        @Query("roleName") roleName: String?,
        @Query("status") status: String?,
        @Query("gender") gender: Int?
    ): Call<UserListResponse>


    @PUT("admin/user-management/user")
    fun updateUser(
        @Header("Authorization") authorization: String,
        @Query("email") email: String,
        @Body updateUserRequest: UpdateUserRequestDTO
    ) : Call<UserListResponse.User>

    @POST("admin/user-management/deactivate-user")
    fun deactivateUser(
        @Header("Authorization") authorization: String,
        @Query("email") email: String
    ): Call<UserListResponse.User>


    @POST("admin/user-management/active-user")
    fun ActivateUser(
        @Header("Authorization") authorization: String,
        @Query("email") email: String
    ): Call<UserListResponse.User>


    @POST("auth/register")
    fun registerUser(
        @Body user: AddUserRequestDTO
    ): Call<String>


    



}