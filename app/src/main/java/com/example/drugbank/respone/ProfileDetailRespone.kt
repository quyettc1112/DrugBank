package com.example.drugbank.respone


import android.os.Parcelable

 data class ProfileDetailRespone(
    val profileInformation: ProfileInformation?,
    val profileDetailList: List<ProfileDetail?>?
) {

     data class ProfileInformation(
        val profileId: Int?,
        val title: String?,
        val createdBy: String?,
        val createdOn: String?,
        val updatedBy: String?,
        val updatedOn: String?,
        val status: String?,
        val imageURL: String?
    )


     data class ProfileDetail(
        val profileDetailId: Int?,
        val productResponseDTO: ProductResponseDTO?,
        val status: String?
    )  {

         data class ProductResponseDTO(
            val id: Int?,
            val labeller: String?,
            val name: String?,
            val route: String?,
            val prescriptionName: String?,
            val drugIngredients: List<DrugIngredient?>?,
            val category: Category?,
            val manufactor: Manufactor?,
            val authorities: List<Authority?>?,
            val pharmacogenomic: Pharmacogenomic?,
            val productAllergyDetail: ProductAllergyDetail?,
            val contraindication: Contraindication?,
            val image: Any?,
            val approvedByANSM: Boolean?,
            val approvedByFDA: Boolean?
        )  {

             data class DrugIngredient(
                val drugId: Int?,
                val name: String?,
                val strength: String?,
                val strengthNumber: String?,
                val strengthUnit: String?,
                val clinicallyRelevant: String?
            )


             data class Category(
                val id: Int?,
                val title: String?,
                val slug: String?
            )


             data class Manufactor(
                val name: String?,
                val company: String?,
                val score: String?,
                val source: String?,
                val countryId: Int?,
                val countryName: String?
            )


             data class Authority(
                val certificateName: String?,
                val countryId: Int?,
                val countryName: String?
            )


             data class Pharmacogenomic(
                val indication: String?,
                val pharmacodynamic: String?,
                val mechanismOfAction: String?,
                val asorption: String?,
                val toxicity: String?
            )


             data class ProductAllergyDetail(
                val detail: String?,
                val summary: String?
            )


             data class Contraindication(
                val relationship: String?,
                val value: String?
            )
        }
    }
}