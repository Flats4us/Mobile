package com.example.flats4us21.data

import android.net.Uri

open class StudentOwner(
    override val id: Int,
    override val name: String,
    override val surname: String,
    override val email: String,
    override val phoneNumber: String,
    open val profilePicture: Uri?,
    open val userStatus: String,
    open val verificationStatus: String,
) : User(id, name, surname, email, phoneNumber)
