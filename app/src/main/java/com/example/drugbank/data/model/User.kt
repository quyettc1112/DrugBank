package com.example.drugbank.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("fullname") val fullname: String,
    @SerializedName("dayOfBirth") val dayOfBirth: String,
    @SerializedName("gender") val gender: Int,
    @SerializedName("roleName") val roleName: String,
    @SerializedName("isActive") val isActive: String
)
