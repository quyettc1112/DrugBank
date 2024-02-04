package com.example.drugbank.data.model

import com.example.drugbank.respone.DrugMListRespone
import com.google.gson.annotations.SerializedName

data class Pageable(
    @SerializedName("offset") val offset: Int,
    @SerializedName("pageNumber") val pageNumber: Int,
    @SerializedName("pageSize") val pageSize: Int,
    @SerializedName("paged") val paged: Boolean,
    @SerializedName("sort") val sort: Sort,
    @SerializedName("unpaged") val unpaged: Boolean
)
