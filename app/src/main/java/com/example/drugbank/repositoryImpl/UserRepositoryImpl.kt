package com.example.drugbank.repositoryImpl

import com.example.drugbank.apiService.Admin_UserM_APIService
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userAPIService: Admin_UserM_APIService
) {

}