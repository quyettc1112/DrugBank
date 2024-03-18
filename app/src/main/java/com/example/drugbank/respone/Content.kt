package com.example.drugbank.respone

data class Content(
    val createdBy: String,
    val createdOn: String,
    val imageURL: String,
    val profileId: Int,
    val status: String,
    val title: String,
    val updatedBy: String,
    val updatedOn: String
)