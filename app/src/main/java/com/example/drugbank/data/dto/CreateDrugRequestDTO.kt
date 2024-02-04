package com.example.drugbank.data.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

data class CreateDrugRequestDTO(
    @SerializedName("approvalStatus") val approvalStatus: Int,
    @SerializedName("clinicalDescription") val clinicalDescription: String,
    @SerializedName("description") val description: String,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("simpleDescription") val simpleDescription: String,
    @SerializedName("state") val state: String,
    @SerializedName("type") val type: String
)
