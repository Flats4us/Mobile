package com.example.flats4us21.data

import android.net.Uri

data class Owner(
    override val id: Int,
    override val name: String,
    override val surname: String,
    override val email: String,
    override val phoneNumber: String,
    override val profilePicture: Uri?,
    override val userStatus: String,
    override val verificationStatus: String
) : StudentOwner(
    id, name, surname, email, phoneNumber, profilePicture, userStatus, verificationStatus
)

