package com.example.flats4us21.data


import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("token")
    val token: String,
    @SerializedName("expiresAt")
    val expiresAt: Int,
    @SerializedName("role")
    val role: String,
    @SerializedName("verificationStatus")
    val verificationStatus: Int
)