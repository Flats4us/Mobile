package com.example.flats4us21.services

import com.example.flats4us21.data.dto.LoginRequest
import com.example.flats4us21.data.dto.LoginResponse
import com.example.flats4us21.data.dto.NewUserDto
import com.example.flats4us21.data.dto.OwnerDTO
import com.example.flats4us21.data.dto.StudentDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("/api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("/s22677/JSON-data-example/main/user")
    suspend fun registerStudent(@Body student: StudentDTO): Response<Void>
    @POST("/s22677/JSON-data-example/main/user")
    suspend fun registerOwner(@Body student: OwnerDTO): Response<Void>

    @POST("/s22677/JSON-data-example/main/user/register")
    suspend fun  registerUser(@Body user: NewUserDto): Response<Void>
}