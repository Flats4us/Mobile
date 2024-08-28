package com.example.flats4us.data.dto

import android.net.Uri
import com.example.flats4us.data.Address
import java.time.LocalDate
import java.time.Year

data class StudentDTO(
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
    val birthDate: Year,
    val studentNumber: String,
    val university: String,
    ) : StudentOwnerDTO(
    name, surname, email, phoneNumber, address, dateOfAccountCreation, hashedPassword, profilePicture, userStatus,
    document, verificationStatus, documentExpirationDate
)