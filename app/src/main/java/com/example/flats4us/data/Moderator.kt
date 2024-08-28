package com.example.flats4us.data

data class Moderator(
    override val id: Int,
    override val name: String,
    override val surname: String,
    override val email: String,
    override val phoneNumber: String,
) : User(id, name, surname, email, phoneNumber)
