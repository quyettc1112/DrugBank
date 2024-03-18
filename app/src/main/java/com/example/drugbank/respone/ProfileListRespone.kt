package com.example.drugbank.respone


import android.os.Parcelable
import com.example.drugbank.data.model.Pageable
import com.example.drugbank.data.model.Sort
import com.google.gson.annotations.SerializedName

data class ProfileListRespone(
    @SerializedName("content") val content: List<Content?>?,
    @SerializedName("pageable") val pageable: Pageable?,
    @SerializedName("totalElements")val totalElements: Int?,
    @SerializedName("totalPages")val totalPages: Int?,
    @SerializedName("last")val last: Boolean?,
    @SerializedName("size")val size: Int?,
    @SerializedName("number")val number: Int?,
    @SerializedName("sort")val sort: Sort?,
    @SerializedName("numberOfElements")val numberOfElements: Int?,
    @SerializedName("first")val first: Boolean?,
    @SerializedName("empty")val empty: Boolean?
)  {
     data class Content(
         @SerializedName("profileId")   val profileId: Int?,
         @SerializedName("title")  val title: String?,
         @SerializedName("createdBy") val createdBy: String?,
         @SerializedName("createdOn")  val createdOn: String?,
         @SerializedName("updatedBy")  val updatedBy: String?,
         @SerializedName("updatedOn")  val updatedOn: String?,
         @SerializedName("status")  val status: String?,
         @SerializedName("imageURL") val imageURL: String?
    )


}