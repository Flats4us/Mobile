package com.example.flats4us21.data.dto

import java.time.LocalDate

data class NewUserDto(
    open val name: String,
    open val surname: String,
    open val city: String,
    open val street: String,
    open val buildingNumber: String,
    open val flatNumber: String,
    open val postalCode: String,
    open val phoneNumber: String,
    open val birthDate: LocalDate,
    open val email: String,
    open val password: String
)
