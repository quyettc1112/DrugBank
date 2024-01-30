package com.example.drugbank.repository

import com.example.drugbank.data.dto.AddUserRequestDTO
import com.example.drugbank.data.dto.UpdateUserRequestDTO
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.FragmentScoped
import retrofit2.http.Header
import retrofit2.http.Query
import javax.inject.Inject

@ActivityScoped
class UserRepository @Inject constructor(
    private val apiService: UserAPIService
) {
    fun getPageableUser(authorization: String,
                         pageNo: Int,
                         pageSize: Int,
                        sortField: String,
                        sortOrder: String,
                        roleName: String?,
                        status: String?,
                        gender: Int?)
    = apiService.getPageableUser(
        authorization = authorization,
        pageNo = pageNo,
        pageSize =pageSize,
        sortField = sortField,
        sortOrder = sortOrder,
        roleName = roleName,
        status = status,
        gender = gender
    )

    fun UpdateUserInfo(
        authorization: String,
        email: String,
        userRequestDTO: UpdateUserRequestDTO
    ) = apiService.updateUser(authorization, email, userRequestDTO)


    fun deactivateUser(
        authorization: String,
        email: String
    ) = apiService.deactivateUser(
        authorization = authorization,
        email =  email)

    fun activateUser(
        authorization: String,
        email: String
    ) = apiService.ActivateUser(
        authorization = authorization,
        email = email)


    fun addUser(
        authorization: String,
        addUserRequestDTO: AddUserRequestDTO
    ) = apiService.registerUser(
        authorization = authorization,
        user = addUserRequestDTO
    )


}