package com.example.flats4us21.data

import com.google.gson.annotations.SerializedName

data class Tenant (
    @SerializedName("rentId")
    val userId: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("fullName")
    val fullName: String
)
