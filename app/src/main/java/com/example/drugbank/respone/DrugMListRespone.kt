package com.example.drugbank.respone


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.example.drugbank.data.model.Pageable
import com.example.drugbank.data.model.Sort

data class DrugMListRespone(
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
  @SerializedName("approvalStatus") val approvalStatus: Int,
  @SerializedName("clinicalDescription") val clinicalDescription: String,
  @SerializedName("description") val description: String,
  @SerializedName("drugbankId") val drugbankId: Any,
  @SerializedName("id") val id: Int,
  @SerializedName("name") val name: String,
  @SerializedName("simpleDescription") val simpleDescription: String,
  @SerializedName("state")val state: String,
  @SerializedName("type") val type: String,
  @SerializedName("active") val active: Boolean
 )


}