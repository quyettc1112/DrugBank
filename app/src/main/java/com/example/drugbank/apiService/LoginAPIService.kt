package com.example.drugbank.apiService

import com.example.drugbank.data.dto.AddUserRequestDTO
import com.example.drugbank.data.model.LoginDTO
import com.example.drugbank.data.model.Token
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginAPIService {

    // Body truyền dư liệu vào là class DAO và responese vể lớp Token
    @POST("auth/login")
    fun login(@Body loginRequest: LoginDTO): Call<Token>

    @GET("api/secure")
    fun getSecureData(@Header("Authorization") authorization: String): Call<String>

    @POST("auth/register")
    fun registerUser(@Body userRequestDTO: AddUserRequestDTO): Call<String>

}