package com.example.drugbank.repository

import com.example.drugbank.apiService.Admin_ProductM_APIService
import com.example.drugbank.apiService.Admin_Profile_APIService
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class Admin_Profile_Repository @Inject constructor(
    private val adminProfileApitService: Admin_Profile_APIService
) {
    fun getProfileList(
        authorization: String,
        pageNo: Int,
        pageSize: Int,
        search: String
    ) = adminProfileApitService.getProfileList(authorization, pageNo, pageSize, search)


    fun getProfileDetail(
        authorization: String,
        id: Int,
    ) = adminProfileApitService.getProfileDetail(authorization, id)


}