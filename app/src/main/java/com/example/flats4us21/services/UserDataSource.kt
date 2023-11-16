package com.example.flats4us21.services

interface UserDataSource {

    suspend fun login(email: String, password: String)
}