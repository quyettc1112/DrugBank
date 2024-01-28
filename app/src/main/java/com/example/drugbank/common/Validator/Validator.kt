package com.example.drugbank.common.Validator

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

object Validator {

    fun isValidFullName(fullName: String): Boolean {
        // Thêm các quy tắc kiểm tra cho tên đầy đủ
        return fullName.isNotBlank() && fullName.length <= 50
    }

    fun isValidDateOfBirth(dateOfBirth: String): Boolean {
        // Thêm các quy tắc kiểm tra cho ngày sinh
        // Ví dụ: Kiểm tra định dạng ngày (đây chỉ là một ví dụ, bạn có thể thay đổi phù hợp)
        return try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateFormat.isLenient = false
            dateFormat.parse(dateOfBirth)
            true
        } catch (e: ParseException) {
            false
        }
    }
}