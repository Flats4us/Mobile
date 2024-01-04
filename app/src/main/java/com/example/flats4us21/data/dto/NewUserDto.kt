package com.example.flats4us21.data.dto

import android.net.Uri
import com.example.flats4us21.data.QuestionResponse
import com.example.flats4us21.data.UserType
import java.time.LocalDate

data class NewUserDto(
    open val userType: UserType,
    open val profilePicture: Uri,
    open val name: String,
    open val surname: String,
    open val address: String,
    open val phoneNumber: String,
    open val links: List<String>,
    open val interest: List<Int>,
    open val birthDate: LocalDate,
    open val university: String,
    open val studentNumber: String,
    open val bankAccount: String,
    open val questionResponseList: List<QuestionResponse>,
    open val documentExpireDate: LocalDate,
    open val questionList: List<QuestionResponse>,
    open val images: List<Uri>,
    open val email: String,
    open val password: String
)
