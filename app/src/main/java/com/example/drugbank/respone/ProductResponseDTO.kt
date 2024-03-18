package com.example.drugbank.respone

data class ProductResponseDTO(
    val approvedByANSM: Boolean,
    val approvedByFDA: Boolean,
    val authorities: List<Authority>,
    val category: Category,
    val contraindication: Contraindication,
    val drugIngredients: List<DrugIngredient>,
    val id: Int,
    val image: Any,
    val labeller: String,
    val manufactor: Manufactor,
    val name: String,
    val pharmacogenomic: Pharmacogenomic,
    val prescriptionName: String,
    val productAllergyDetail: ProductAllergyDetail,
    val route: String
)