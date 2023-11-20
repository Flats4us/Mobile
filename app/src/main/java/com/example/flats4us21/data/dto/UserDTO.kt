package com.example.flats4us21.data.dto

import com.example.flats4us21.data.Address
import java.time.LocalDate

open class UserDTO(
    open val name : String,
    open val surname : String,
    open val email : String,
    open val phoneNumber: String,
    open val address: Address,
    open val dateOfAccountCreation: LocalDate,
    open val hashedPassword : String
)
