package com.example.flats4us21.data.dto

data class LoginRequest(
    val email: String,
    val password: String,
    val fcmToken: String
)
