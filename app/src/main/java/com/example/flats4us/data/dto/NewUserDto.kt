package com.example.flats4us.data.dto

import android.net.Uri
import com.example.flats4us.data.QuestionResponse
import com.example.flats4us.data.UserType
import java.time.LocalDate

data class NewUserDto(
    val userType: UserType,
    val name: String,
    val surname: String,
    val address: String,
    val phoneNumber: String,
    val links: List<String>,
    val interest: List<Int>,
    val birthDate: LocalDate?,
    val university: String?,
    val studentNumber: String?,
    val bankAccount: String?,
    val documentNumber: String?,
    val documentExpireDate: LocalDate?,
    val questionResponseList: List<QuestionResponse>,
    val document: List<Uri>,
    val email: String,
    val password: String,
    val repeatPassword: String
)
