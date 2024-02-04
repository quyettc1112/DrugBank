package com.example.drugbank.repository

import com.example.drugbank.data.dto.CreateDrugRequestDTO
import com.example.drugbank.data.dto.UpdateDrugRequestDTO
import dagger.hilt.android.scopes.ActivityScoped
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Query
import javax.inject.Inject

@ActivityScoped
class Admin_DrugM_Repository @Inject constructor(
    private val A_DM_API_service: Admin_DrugM_APIService
) {
    fun getDrugMList(
        authorization: String,
        pageNo: Int,
        pageSize: Int,
        sortField: String,
        sortOrder: String,
        search: String
    ) = A_DM_API_service.getDrugMList(authorization, pageNo, pageSize, sortField, sortOrder, search)

    fun createDrug(
        authorization: String,
        createDrugRequestDTO: CreateDrugRequestDTO
    ) = A_DM_API_service.createDrug(authorization, createDrugRequestDTO)

    fun updateDrug(
        authorization: String,
        drugId: Int,
        updateDrugRequestDTO: UpdateDrugRequestDTO
    ) = A_DM_API_service.updateDrugbyID(authorization, drugId, updateDrugRequestDTO)

    fun deleteDrug(
        authorization: String,
        id: Int
    ) = A_DM_API_service.deleteDrug(authorization, id)

}