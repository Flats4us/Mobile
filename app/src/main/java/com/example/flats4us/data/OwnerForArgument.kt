package com.example.flats4us.data


import com.google.gson.annotations.SerializedName

data class OwnerForArgument(
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("profilePicture")
    val profilePicture: Image
)