package com.example.drugbank.apiService



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
    @GET("admin/users")
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


    @PUT("admin/users")
    fun updateUser(
        @Header("Authorization") authorization: String,
        @Query("email") email: String,
        @Body updateUserRequest: UpdateUserRequestDTO
    ) : Call<UserListResponse.User>

    @POST("admin/users/deactivate")
    fun deactivateUser(
        @Header("Authorization") authorization: String,
        @Query("email") email: String
    ): Call<UserListResponse.User>


    @POST("admin/users/activate")
    fun ActivateUser(
        @Header("Authorization") authorization: String,
        @Query("email") email: String
    ): Call<UserListResponse.User>


    @POST("auth/register")
    fun registerUser(
        @Header("Authorization") authorization: String,
        @Body user: AddUserRequestDTO
    ): Call<String>


    @GET("admin/users/email")
    fun getUserByEmail (
        @Header("Authorization") authorization: String,
        @Query("email") email: String
    ): Call<UserListResponse.User>




    



}