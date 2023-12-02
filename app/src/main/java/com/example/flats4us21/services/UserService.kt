package com.example.flats4us21.services

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("/s22677/JSON-data-example/main/user")
    suspend fun login(@Body email: String, @Body password: String): Response<Void>
}