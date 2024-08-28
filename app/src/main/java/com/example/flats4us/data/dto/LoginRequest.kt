package com.example.flats4us.data.dto

data class LoginRequest(
    val email: String,
    val password: String,
    val fcmToken: String
)
