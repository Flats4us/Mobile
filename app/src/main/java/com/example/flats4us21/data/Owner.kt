package com.example.flats4us21.data

import android.net.Uri
import com.google.gson.annotations.SerializedName

data class Owner(
    @SerializedName("ownerId")
    override val id: Int,
    @SerializedName("ownerName")
    override val name: String,
    @SerializedName("ownerSurname")
    override val surname: String,
    @SerializedName("ownerEmail")
    override val email: String,
    @SerializedName("ownerPhoneNumber")
    override val phoneNumber: String,
    @SerializedName("ownerProfilePicture")
    override val profilePicture: Uri?,
    @SerializedName("ownerUserStatus")
    override val userStatus: String,
    @SerializedName("ownerVerificationStatus")
    override val verificationStatus: String
) : StudentOwner(
    id, name, surname, email, phoneNumber, profilePicture, userStatus, verificationStatus
)
