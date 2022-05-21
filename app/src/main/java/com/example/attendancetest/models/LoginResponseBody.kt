package com.example.attendancetest.models

data class LoginResponseBody(
    val access: String,
    val is_admin: String,
    val is_multi: String,
    val refresh: String,
    val user_type: String
)