package com.example.drugbank.repository

import dagger.hilt.android.scopes.ActivityScoped
import okhttp3.MultipartBody
import retrofit2.http.Part
import javax.inject.Inject

@ActivityScoped
class API_User_Repository @Inject constructor(
    private val apiService: API_User_Service
){
    fun uploadImage(
        authorization: String,
        email:String?,
        image: MultipartBody.Part
    ) =  apiService.uploadImage(authorization,email, image)



    fun upLoadImageProduct(
        authorization: String,
        ApprovalProductID:Int?,
        image: MultipartBody.Part
    ) = apiService.uploadImageProduct(authorization, ApprovalProductID, image)
}