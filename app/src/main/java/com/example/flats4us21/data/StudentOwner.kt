package com.example.flats4us21.data

open class StudentOwner(
    override val id: Int,
    override val name: String,
    override val surname: String,
    override val email: String,
    override val phoneNumber: String,
    open val profilePicture: Image?
) : User(id, name, surname, email, phoneNumber)
