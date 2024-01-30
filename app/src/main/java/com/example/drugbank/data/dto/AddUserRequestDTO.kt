package com.example.drugbank.data.dto

import com.google.gson.annotations.SerializedName

class AddUserRequestDTO(
    val email: String,
    val username: String,
    val fullname: String,
    val dob: String,
    val gender: Int,
    val roleId: Int
) {
}