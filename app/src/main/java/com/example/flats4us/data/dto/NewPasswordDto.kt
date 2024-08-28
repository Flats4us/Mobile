package com.example.flats4us.data.dto


import com.google.gson.annotations.SerializedName

data class NewPasswordDto(
    @SerializedName("oldPassword")
    val oldPassword: String,
    @SerializedName("newPassword")
    val newPassword: String
)