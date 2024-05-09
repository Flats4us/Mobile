package com.example.flats4us21.services

import com.example.flats4us21.data.NewPropertyApiResponse
import com.example.flats4us21.data.Profile
import com.example.flats4us21.data.dto.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {

    @POST("/api/auth/login")
    suspend fun login(@Body loginRequest : LoginRequest): Response<LoginResponse>

    @POST("/s22677/JSON-data-example/main/user")
    suspend fun registerStudent(@Body student: StudentDTO): Response<Void>
    @POST("/s22677/JSON-data-example/main/user")
    suspend fun registerOwner(@Body student: OwnerDTO): Response<Void>

    @POST("/s22677/JSON-data-example/main/user/register")
    suspend fun  registerUser(@Body user: NewUserDto): Response<Void>

    @GET("/api/users/{email}")
    suspend fun checkEmail(@Path("email", encoded = true) email: String): Response<NewPropertyApiResponse<Boolean>>

    @GET("api/users/my-profile")
    suspend fun  getProfile(): Response<Profile>
}