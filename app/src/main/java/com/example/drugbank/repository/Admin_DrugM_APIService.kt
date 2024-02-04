package com.example.drugbank.repository

import com.example.drugbank.respone.DrugMListRespone
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface Admin_DrugM_APIService {

    @GET("admin/drug-management/drugs")
    fun getDrugMList(
        @Header("Authorization") authorization: String,
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Query("sortField") sortField: String,
        @Query("sortOrder") sortOrder: String,
        @Query("search") search: String,
    ): Call<DrugMListRespone>

}