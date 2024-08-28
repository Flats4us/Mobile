package com.example.flats4us.data

import com.google.gson.annotations.SerializedName

data class Owner(
    @SerializedName("userId")
    override val id: Int,
    @SerializedName("name")
    override val name: String,
    @SerializedName("surname")
    override val surname: String,
    @SerializedName("email")
    override val email: String,
    @SerializedName("phoneNumber")
    override val phoneNumber: String,
    @SerializedName("profilePicture")
    override val profilePicture: Image?
) : StudentOwner(
    id, name, surname, email, phoneNumber, profilePicture
)
