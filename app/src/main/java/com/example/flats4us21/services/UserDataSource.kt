package com.example.flats4us21.services

import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.MyProfile
import com.example.flats4us21.data.Profile
import com.example.flats4us21.data.dto.LoginResponse
import com.example.flats4us21.data.dto.NewUserDto
import com.example.flats4us21.data.dto.UpdateMyProfileDto

interface UserDataSource {

    suspend fun login(email: String, password: String): ApiResult<LoginResponse?>

    suspend fun register(user: NewUserDto)

    suspend fun checkEmail(email: String): ApiResult<Boolean>

    suspend fun getProfile(): ApiResult<MyProfile>

    suspend fun getProfile(id: Int): ApiResult<Profile>

    suspend fun sendPasswordResetLink(email: String): ApiResult<String>

    suspend fun updateMyProfile(updateMyProfileDto: UpdateMyProfileDto): ApiResult<String>
}