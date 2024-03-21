package com.example.drugbank.respone


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.example.drugbank.data.model.Authorities
import com.example.drugbank.data.model.DrugIn


data class ProductDetailRespone(
 @SerializedName("authorities") val authorities: List<Authorities>,
 @SerializedName("category") val category: Category,
 @SerializedName("contraindication") val contraindication: Contraindication?,
 @SerializedName("drugIngredients") val drugIngredients: List<DrugIn>,
 @SerializedName("id") val id: Int,
 @SerializedName("labeller") val labeller: String?,
 @SerializedName("manufactor") val manufactor: Manufactor?,
 @SerializedName("name") val name: String?,
 @SerializedName("pharmacogenomic") val pharmacogenomic: Pharmacogenomic?,
 @SerializedName("prescriptionName") val prescriptionName: String?,
 @SerializedName("productAllergyDetail") val productAllergyDetail: ProductAllergyDetail?,
 @SerializedName("route") val route: String?,
 @SerializedName("image") val image: String?,
 @SerializedName("productAdministrationDTO") val productAdministrationDTO: ProductAdministrationDTO?,
) {
 @Keep
 data class Category(
  @SerializedName("id")
  val id: Int?,
  @SerializedName("slug")
  val slug: String?,
  @SerializedName("title")
  val title: String?
 )

 @Keep
 data class Contraindication(
  @SerializedName("relationship")
  val relationship: String?,
  @SerializedName("value")
  val value: String?
 )

 @Keep
 data class Manufactor(
  @SerializedName("company")
  val company: String?,
  @SerializedName("countryId")
  val countryId: Int?,
  @SerializedName("countryName")
  val countryName: String?,
  @SerializedName("name")
  val name: String?,
  @SerializedName("score")
  val score: Any?,
  @SerializedName("source")
  val source: String?
 )

 data class Pharmacogenomic(
  @SerializedName("asorption") val asorption: String?,
  @SerializedName("indication")
  val indication: String?,
  @SerializedName("mechanismOfAction")
  val mechanismOfAction: String?,
  @SerializedName("pharmacodynamic")
  val pharmacodynamic: String?,
  @SerializedName("toxicity")
  val toxicity: String?
 )

 @Keep
 data class ProductAllergyDetail(
  @SerializedName("detail")
  val detail: String?,
  @SerializedName("summary")
  val summary: String?
 )

data class  ProductAdministrationDTO(
 @SerializedName("id") val id: Int?,
 @SerializedName("name") val name: String?,
)

 data class  Image (
  @SerializedName("image") val image: String?
 )
}