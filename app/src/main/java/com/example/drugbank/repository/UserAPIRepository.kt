package com.example.drugbank.repository

import com.example.drugbank.data.api.UserAPIService
import javax.inject.Inject

class UserAPIRepository @Inject constructor(
    private val userAPIService: UserAPIService
) {

}