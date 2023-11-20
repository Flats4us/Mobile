package com.example.flats4us21.data.dto

import android.net.Uri
import com.example.flats4us21.data.Address
import java.time.LocalDate

open class StudentOwnerDTO (
    override val name: String,
    override val surname: String,
    override val email: String,
    override val phoneNumber: String,
    override val address: Address,
    override val dateOfAccountCreation: LocalDate,
    override val hashedPassword : String,
    open val profilePicture: Uri?,
    open val userStatus: String,
    open val document: Uri?,
    open val verificationStatus: String,
    open val documentExpirationDate: LocalDate,
) : UserDTO(name, surname, email, phoneNumber, address, dateOfAccountCreation, hashedPassword)
