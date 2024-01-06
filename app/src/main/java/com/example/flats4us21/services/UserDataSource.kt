package com.example.flats4us21.services

import com.example.flats4us21.data.dto.NewUserDto

interface UserDataSource {

    suspend fun login(email: String, password: String)

    suspend fun register(user: NewUserDto)
}