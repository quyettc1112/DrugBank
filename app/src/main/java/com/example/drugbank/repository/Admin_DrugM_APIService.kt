package com.example.drugbank.repository

import com.example.drugbank.data.dto.CreateDrugRequestDTO
import com.example.drugbank.data.dto.UpdateDrugRequestDTO
import com.example.drugbank.respone.DrugMListRespone
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
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



    @PUT("admin/drug-management/drug/update")
    fun updateDrugbyID(
        @Header("Authorization") authorization: String,
        @Query("drugId") drugId: Int,
        @Body updateDrugRequestDTO: UpdateDrugRequestDTO
    ): Call<DrugMListRespone.Content>


    @POST("admin/drug-management/drug/create")
    fun createDrug(
        @Header("Authorization") authorization: String,
        @Body createDrugRequestDTO: CreateDrugRequestDTO
    ): Call<Void>

    @DELETE("admin/drug-management/drug/delete")
    fun deleteDrug(
        @Header("Authorization") authorization: String,
        @Query("id") id: Int
    ):Call<Void>


}