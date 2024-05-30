package com.example.flats4us21.services

import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.MyProfile
import com.example.flats4us21.data.Profile
import com.example.flats4us21.data.dto.LoginResponse
import com.example.flats4us21.data.dto.NewOwnerDto
import com.example.flats4us21.data.dto.NewStudentDto
import com.example.flats4us21.data.dto.NewUserOpinionDto
import com.example.flats4us21.data.dto.UpdateMyProfileDto

interface UserDataSource {

    suspend fun login(email: String, password: String): ApiResult<LoginResponse?>

    suspend fun registerStudent(user: NewStudentDto): ApiResult<LoginResponse?>

    suspend fun registerOwner(user: NewOwnerDto): ApiResult<LoginResponse?>

    suspend fun checkEmail(email: String): ApiResult<Boolean>

    suspend fun getProfile(): ApiResult<MyProfile>

    suspend fun getProfile(id: Int): ApiResult<Profile>

    suspend fun sendPasswordResetLink(email: String): ApiResult<String>

    suspend fun updateMyProfile(updateMyProfileDto: UpdateMyProfileDto): ApiResult<String>

    suspend fun addOpinion(targetUserId: Int,  newUserOpinionDto : NewUserOpinionDto): ApiResult<String>

}