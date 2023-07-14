package com.example.flats4us21.data

import java.time.LocalDate

data class Student(
    override val name : String,
    override val surname : String,
    override val email : String,
    val birthDate : LocalDate,
    val studentNumber : String,
    val university : String,
    ) : User(name, surname, email)
