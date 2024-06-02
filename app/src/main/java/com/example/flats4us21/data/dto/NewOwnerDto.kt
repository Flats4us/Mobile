package com.example.flats4us21.data.dto


import com.google.gson.annotations.SerializedName

data class NewOwnerDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("surname")
    val surname: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("documentType")
    val documentType: Int,
    @SerializedName("documentExpireDate")
    val documentExpireDate: String,
    @SerializedName("bankAccount")
    val bankAccount: String,
    @SerializedName("documentNumber")
    val documentNumber: String
)