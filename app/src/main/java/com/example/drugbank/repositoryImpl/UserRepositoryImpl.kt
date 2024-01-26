package com.example.drugbank.repositoryImpl

import com.example.drugbank.repository.UserAPIService
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userAPIService: UserAPIService
) {

}