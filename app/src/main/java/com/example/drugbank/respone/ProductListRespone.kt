package com.example.drugbank.respone


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.example.drugbank.data.model.Pageable
import com.example.drugbank.data.model.Sort

data class ProductListRespone(
 @SerializedName("content") val content: List<Content>,
 @SerializedName("empty") val empty: Boolean,
 @SerializedName("first") val first: Boolean,
 @SerializedName("last") val last: Boolean,
 @SerializedName("number") val number: Int,
 @SerializedName("numberOfElements") val numberOfElements: Int,
 @SerializedName("pageable") val pageable: Pageable,
 @SerializedName("size") val size: Int,
 @SerializedName("sort") val sort: Sort,
 @SerializedName("totalElements") val totalElements: Int,
 @SerializedName("totalPages") val totalPages: Int
) {

 data class Content(
  @SerializedName("category") val category: String,
  @SerializedName("company") val company: String,
  @SerializedName("createdOn") val createdOn: String,
  @SerializedName("id") val id: Int,
  @SerializedName("labeller") val labeller: String,
  @SerializedName("name") val name: String,
  @SerializedName("prescriptionName") val prescriptionName: String,
  @SerializedName("route") val route: String?,
  @SerializedName("image") val image: String?,
  @SerializedName("productAdministration") val productAdministration: ProductAdministration?,
  
 )

 data class ProductAdministration(
  @SerializedName("id") val id: Int?,
  @SerializedName("name") val name: String?
 )


}