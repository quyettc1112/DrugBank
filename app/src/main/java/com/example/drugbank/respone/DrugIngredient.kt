package com.example.drugbank.respone

data class DrugIngredient(
    val clinicallyRelevant: String,
    val drugId: Int,
    val name: String,
    val strength: String,
    val strengthNumber: String,
    val strengthUnit: String
)