package com.example.flats4us.services

import com.example.flats4us.data.ApiResult
import com.example.flats4us.data.MyProfile
import com.example.flats4us.data.Profile
import com.example.flats4us.data.dto.LoginResponse
import com.example.flats4us.data.dto.NewOwnerDto
import com.example.flats4us.data.dto.NewPasswordDto
import com.example.flats4us.data.dto.NewStudentDto
import com.example.flats4us.data.dto.NewUserOpinionDto
import com.example.flats4us.data.dto.UpdateMyProfileDto
import java.io.File

interface UserDataSource {

    suspend fun login(email: String, password: String, token: String): ApiResult<LoginResponse?>

    suspend fun registerStudent(user: NewStudentDto): ApiResult<LoginResponse?>

    suspend fun registerOwner(user: NewOwnerDto): ApiResult<LoginResponse?>

    suspend fun checkEmail(email: String): ApiResult<Boolean>

    suspend fun getProfile(): ApiResult<MyProfile>

    suspend fun getProfile(id: Int): ApiResult<Profile>

    suspend fun sendPasswordResetLink(email: String): ApiResult<String>

    suspend fun updateMyProfile(updateMyProfileDto: UpdateMyProfileDto): ApiResult<String>

    suspend fun addUserFiles(profilePicture: File?, document: File?): ApiResult<String>


    suspend fun addOpinion(targetUserId: Int,  newUserOpinionDto : NewUserOpinionDto): ApiResult<String>

    suspend fun changePassword(newPasswordDto: NewPasswordDto): ApiResult<String>

    suspend fun changeEmail(email: String): ApiResult<String>

    suspend fun logout(): ApiResult<String>

}