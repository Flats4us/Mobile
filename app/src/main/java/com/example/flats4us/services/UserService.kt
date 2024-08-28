package com.example.flats4us.services

import com.example.flats4us.data.MyProfile
import com.example.flats4us.data.NewPropertyApiResponse
import com.example.flats4us.data.Profile
import com.example.flats4us.data.dto.LoginRequest
import com.example.flats4us.data.dto.LoginResponse
import com.example.flats4us.data.dto.NewOwnerDto
import com.example.flats4us.data.dto.NewPasswordDto
import com.example.flats4us.data.dto.NewStudentDto
import com.example.flats4us.data.dto.NewUserOpinionDto
import com.example.flats4us.data.dto.UpdateMyProfileDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface UserService {

    @POST("/api/auth/login")
    suspend fun login(@Body loginRequest : LoginRequest): Response<LoginResponse>

    @POST("/api/auth/register/students")
    suspend fun registerStudent(@Body student: NewStudentDto): Response<LoginResponse>
    @POST("/api/auth/register/owners")
    suspend fun registerOwner(@Body student: NewOwnerDto): Response<LoginResponse>

    @GET("/api/users/{email}")
    suspend fun checkEmail(@Path("email", encoded = true) email: String): Response<NewPropertyApiResponse<Boolean>>

    @GET("api/users/my-profile")
    suspend fun getProfile(): Response<MyProfile>

    @GET("api/users/{id}/profile")
    suspend fun getProfile(@Path("id") id: Int) : Response<Profile>

    @POST("api/auth/{email}/send-password-reset-link")
    suspend fun sendPasswordResetLink(@Path("email") email: String): Response<NewPropertyApiResponse<String>>

    @PUT("api/users/current")
    suspend fun updateMyProfile(@Body updateMyProfileDto: UpdateMyProfileDto) : Response<ResponseBody>

    @Multipart
    @POST("api/users/files")
    suspend fun addUserFiles(@Part profilePicture: MultipartBody.Part?, @Part document: MultipartBody.Part?): Response<NewPropertyApiResponse<String>>

    @POST("api/users/{targetUserId}/opinion")
    suspend fun addOpinion(@Path("targetUserId") targetUserId: Int, @Body newUserOpinionDto : NewUserOpinionDto): Response<NewPropertyApiResponse<String>>

    @PUT("api/auth/change-password")
    suspend fun changePassword(@Body newPasswordDto: NewPasswordDto): Response<NewPropertyApiResponse<String>>

    @PUT("api/users/current")
    suspend fun changeEmail(@Body email: RequestBody): Response<ResponseBody>

    @PATCH("api/auth/logout")
    suspend fun logout(): Response<NewPropertyApiResponse<String>>
}