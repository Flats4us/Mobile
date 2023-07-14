package com.example.flats4us21.data

import java.time.LocalDate

data class Owner(
    override val name : String,
    override val surname : String,
    override val email : String
    ) : User(name, surname, email)
