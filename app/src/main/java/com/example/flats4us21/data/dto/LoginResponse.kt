package com.example.flats4us21.data.dto

data class LoginResponse(
    val token : String,
    val expiresAt : Long,
    val role : String,
    val verificationStatus : Int
)
