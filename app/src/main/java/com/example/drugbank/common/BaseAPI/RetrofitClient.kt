package com.example.drugbank.common.BaseAPI

import com.example.drugbank.apiService.LoginAPIService
import com.example.drugbank.apiService.Admin_UserM_APIService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClient{
    private const val BASE_URL = BaseAPI.BASE_URL

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val gson = GsonBuilder().setLenient().create()
    val instance: LoginAPIService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(client)
            .build()
        retrofit.create(LoginAPIService::class.java)
    }

    val instance_Test: LoginAPIService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            //.addConverterFactory(ScalarsConverterFactory.create())
            .client(client)
            .build()
        retrofit.create(LoginAPIService::class.java)
    }

    val instance_User: Admin_UserM_APIService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
        retrofit.create(Admin_UserM_APIService::class.java)
    }









}