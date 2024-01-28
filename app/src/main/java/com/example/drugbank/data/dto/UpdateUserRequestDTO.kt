package com.example.drugbank.data.dto

import androidx.annotation.NonNull

data class UpdateUserRequestDTO(
    val fullName: String?,
    val dayOfBirth: String?,
    val gender: Int?
) {
    init {
        requireNotNull(fullName) { "fullName must not be null" }
        requireNotNull(dayOfBirth) { "dayOfBirth must not be null" }
        requireNotNull(gender) { "gender must not be null" }
    }
}