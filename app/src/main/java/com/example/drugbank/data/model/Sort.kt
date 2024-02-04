package com.example.drugbank.data.model

import com.google.gson.annotations.SerializedName

data class Sort(
    @SerializedName("sorted")
    val sorted: Boolean,
    @SerializedName("empty")
    val empty: Boolean,
    @SerializedName("unsorted")
    val unsorted: Boolean
)
