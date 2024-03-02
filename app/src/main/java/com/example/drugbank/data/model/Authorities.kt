package com.example.drugbank.data.model

import com.google.gson.annotations.SerializedName

data class Authorities(
    @SerializedName("certificateName") val certificateName: String?,
    @SerializedName("countryId") val countryId: Int?,
    @SerializedName("countryName") val countryName: String?
)
