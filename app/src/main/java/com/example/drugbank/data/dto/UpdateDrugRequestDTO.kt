package com.example.drugbank.data.dto

import com.google.gson.annotations.SerializedName

data class UpdateDrugRequestDTO(
    @SerializedName("type") val type: String,
    @SerializedName("name") val name: String,
    @SerializedName("state") val state: String,
    @SerializedName("simpleDescription") val simpleDescription: String,
    @SerializedName("clinicalDescription") val clinicalDescription: Int,
    @SerializedName("approvalStatus") val approvalStatus: Int,
    @SerializedName("active") val active: Boolean,
)
