package com.example.flats4us21.data

import android.net.Uri
import java.time.LocalDate

data class Moderator(
    override val id: Int,
    override val name: String,
    override val surname: String,
    override val email: String,
    override val phoneNumber: String,
    override val address: Address,
    override val dateOfAccountCreation: LocalDate,
    override val hashedPassword : String
) : User(id, name, surname, email, phoneNumber, address, dateOfAccountCreation, hashedPassword)
