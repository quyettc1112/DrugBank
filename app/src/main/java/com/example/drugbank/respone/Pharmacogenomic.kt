package com.example.drugbank.respone

data class Pharmacogenomic(
    val asorption: String,
    val indication: String,
    val mechanismOfAction: String,
    val pharmacodynamic: String,
    val toxicity: String
)