package com.example.flats4us21.data.dto


import com.google.gson.annotations.SerializedName

data class UpdateMyProfileDto(
    @SerializedName("address")
    val address: String,
    @SerializedName("bankAccount")
    val bankAccount: String?,
    @SerializedName("birthDate")
    val birthDate: String?,
    @SerializedName("documentExpireDate")
    val documentExpireDate: String?,
    @SerializedName("documentNumber")
    val documentNumber: String?,
    @SerializedName("email")
    val email: String,
    @SerializedName("links")
    val links: List<String>,
    @SerializedName("name")
    val name: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("studentNumber")
    val studentNumber: String?,
    @SerializedName("surname")
    val surname: String,
    @SerializedName("university")
    val university: String?
)