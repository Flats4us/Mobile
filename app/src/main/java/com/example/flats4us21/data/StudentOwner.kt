package com.example.flats4us21.data

open class StudentOwner(
    override val id: Int,
    override val name: String,
    override val surname: String,
    override val email: String,
    override val phoneNumber: String,
    open val profilePicture: Image?,
    open val activityStatus: Boolean,
) : User(id, name, surname, email, phoneNumber)
