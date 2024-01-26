package com.example.drugbank.respone
import com.google.gson.annotations.SerializedName


data class UserListResponse(
 @SerializedName("content") val content: List<User>,
 @SerializedName("pageable") val pageable: Pageable,
 @SerializedName("totalPages") val totalPages: Int,
 @SerializedName("totalElements") val totalElements: Int,
 @SerializedName("last") val last: Boolean,
 @SerializedName("size") val size: Int,
 @SerializedName("number") val number: Int,
 @SerializedName("sort") val sort: Sort,
 @SerializedName("numberOfElements") val numberOfElements: Int,
 @SerializedName("first") val first: Boolean,
 @SerializedName("empty") val empty: Boolean,

) {
 data class User(
  @SerializedName("id")
  val id: Int,
  @SerializedName("username")
  val username: String,
  @SerializedName("email")
  val email: String,
  @SerializedName("fullname")
  val fullname: String,
  @SerializedName("dayOfBirth")
  val dayOfBirth: String,
  @SerializedName("gender")
  val gender: Int,
  @SerializedName("roleName")
  val roleName: String,
  @SerializedName("isActive")
  val isActive: String
 )

 data class Pageable(
  @SerializedName("pageNumber")
  val pageNumber: Int,
  @SerializedName("pageSize")
  val pageSize: Int,
  @SerializedName("sort")
  val sort: Sort,
  @SerializedName("offset")
  val offset: Int,
  @SerializedName("paged")
  val paged: Boolean,
  @SerializedName("unpaged")
  val unpaged: Boolean
 )

 data class Sort(
  @SerializedName("sorted")
  val sorted: Boolean,
  @SerializedName("empty")
  val empty: Boolean,
  @SerializedName("unsorted")
  val unsorted: Boolean
 )
}