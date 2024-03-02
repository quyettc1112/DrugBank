package com.example.drugbank.data.model

import com.google.gson.annotations.SerializedName

data class DrugIn(
    @SerializedName("drugId") val drugId: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("strength") val strength: String?,
    @SerializedName("strengthNumber") val strengthNumber: String?,
    @SerializedName("strengthUnit") val strengthUnit: String?,
    @SerializedName("clinicallyRelevant") val clinicallyRelevant: String?
)
