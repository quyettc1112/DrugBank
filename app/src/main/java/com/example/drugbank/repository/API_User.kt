package com.example.drugbank.repository

import retrofit2.http.Multipart
import retrofit2.http.POST

interface API_User {

    @Multipart
    @POST("api/storage/user")
    fun uploaduserImage() {



    }
}