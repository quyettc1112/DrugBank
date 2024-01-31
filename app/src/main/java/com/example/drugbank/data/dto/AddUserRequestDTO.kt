package com.example.drugbank.data.dto

import com.google.gson.annotations.SerializedName

data class AddUserRequestDTO(
@SerializedName("email") val email: String,
@SerializedName("username") val username: String,
@SerializedName("fullName") val fullName: String,
@SerializedName("dob") val dob: String,
@SerializedName("gender") val gender: Int,
@SerializedName("roleID") val roleID: Int
)
