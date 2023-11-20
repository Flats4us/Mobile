package com.example.flats4us21.data.dto

import android.net.Uri
import com.example.flats4us21.data.Address
import java.time.LocalDate

data class OwnerDTO(
    override val name: String,
    override val surname: String,
    override val email: String,
    override val phoneNumber: String,
    override val address: Address,
    override val dateOfAccountCreation: LocalDate,
    override val hashedPassword: String,
    override val profilePicture: Uri?,
    override val userStatus: String,
    override val document: Uri?,
    override val verificationStatus: String,
    override val documentExpirationDate: LocalDate,
    val bankAccountNumber: String
) : StudentOwnerDTO(
    name, surname, email, phoneNumber, address, dateOfAccountCreation, hashedPassword, profilePicture, userStatus,
    document, verificationStatus, documentExpirationDate
)