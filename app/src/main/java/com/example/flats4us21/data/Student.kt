package com.example.flats4us21.data

import java.time.Year

data class Student(
    override val id: Int,
    override val name: String,
    override val surname: String,
    override val email: String,
    override val phoneNumber: String,
    override val profilePicture: Image?,
    val birthDate: Year,
    val studentNumber: String,
    val university: String,
) : StudentOwner(
    id, name, surname, email, phoneNumber, profilePicture
)