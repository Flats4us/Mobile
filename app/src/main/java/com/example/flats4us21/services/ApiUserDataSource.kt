package com.example.flats4us21.services

import android.util.Log
import com.example.flats4us21.URL
import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.MyProfile
import com.example.flats4us21.data.Profile
import com.example.flats4us21.data.dto.LoginRequest
import com.example.flats4us21.data.dto.LoginResponse
import com.example.flats4us21.data.dto.NewOwnerDto
import com.example.flats4us21.data.dto.NewPasswordDto
import com.example.flats4us21.data.dto.NewStudentDto
import com.example.flats4us21.data.dto.NewUserOpinionDto
import com.example.flats4us21.data.dto.UpdateMyProfileDto
import com.example.flats4us21.interceptors.AuthInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URLEncoder

private const val TAG = "ApiUserDataSource"
object ApiUserDataSource: UserDataSource{


    val gson: Gson = GsonBuilder()
        .create()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val okHttpClientWithAuthInterceptor = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(AuthInterceptor())
        .build()

    private val api: UserService by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(UserService::class.java)
    }

    private val apiWithAuthInterceptor: UserService by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .client(okHttpClientWithAuthInterceptor)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(UserService::class.java)
    }

    override suspend fun login(email: String, password: String): ApiResult<LoginResponse?> {
        return try {
            val loginRequest = LoginRequest(email, password)
            val response = api.login(loginRequest)
            if(response.isSuccessful) {
                val data = response.body()
                ApiResult.Success(data)
            } else {
                ApiResult.Error("Failed to fetch data: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            ApiResult.Error("An error occurred: ${e.message}")
        }
    }

    override suspend fun registerStudent( user: NewStudentDto): ApiResult<LoginResponse?> {
        return try {
            val response = api.registerStudent(user)
            if(response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    ApiResult.Success(data)
                } else {
                    ApiResult.Error("Response body is null")
                }
            } else {
                ApiResult.Error("Failed to fetch data: ${response.message()}")
            }
        }  catch (e: Exception) {
            ApiResult.Error("An internal error occurred: ${e.message}")
        }
    }

    override suspend fun registerOwner( user: NewOwnerDto): ApiResult<LoginResponse?> {
        return try {
            val response = api.registerOwner(user)
            if(response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    ApiResult.Success(data)
                } else {
                    ApiResult.Error("Response body is null")
                }
            } else {
                ApiResult.Error("Failed to fetch data: ${response.message()}")
            }
        }  catch (e: Exception) {
            ApiResult.Error("An internal error occurred: ${e.message}")
        }
    }

    override suspend fun checkEmail(email: String): ApiResult<Boolean> {
        return try {
            val encodedEmail = withContext(Dispatchers.IO) {
                URLEncoder.encode(email, "UTF-8")
            }
            Log.i(TAG, "Encoded email: $encodedEmail")
            val response = api.checkEmail(encodedEmail)
            if(response.isSuccessful) {
                val data = response.body()!!.result
                ApiResult.Success(data)
            } else {
                ApiResult.Error("Failed to fetch data: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            ApiResult.Error("An error occurred: ${e.message}")
        }
    }

    override suspend fun getProfile(): ApiResult<MyProfile> {
        return try {
            val response = apiWithAuthInterceptor.getProfile()
            if(response.isSuccessful) {
                val data = response.body()!!
                ApiResult.Success(data)
            } else {
                ApiResult.Error("Failed to get profile: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            ApiResult.Error("An error occurred in getting profile information: ${e.message}")
        }
    }

    override suspend fun getProfile(id: Int): ApiResult<Profile> {
        return try {
            val response = apiWithAuthInterceptor.getProfile(id)
            if(response.isSuccessful) {
                val data = response.body()!!
                ApiResult.Success(data)
            } else {
                ApiResult.Error("Failed to get profile: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            ApiResult.Error("An error occurred in getting profile information: ${e.message}")
        }
    }

    override suspend fun sendPasswordResetLink(email: String): ApiResult<String> {
        return try {
            val response = api.sendPasswordResetLink(email)
            if(response.isSuccessful) {
                val data = response.body()!!.result
                ApiResult.Success(data)
            } else
                ApiResult.Error("Failed to send password reset link: ${response.errorBody()?.string()}")
        } catch (e: Exception) {
            ApiResult.Error("An error occurred in sending password reset link: ${e.message}")
        }
    }

    override suspend fun updateMyProfile(updateMyProfileDto: UpdateMyProfileDto): ApiResult<String> {
        return try {
            val response = apiWithAuthInterceptor.updateMyProfile(updateMyProfileDto)
            if(response.isSuccessful) {
                val data = response.body()?.string() ?: "Empty response"
                ApiResult.Success(data)
            } else
                ApiResult.Error("Failed to update profile: ${response.errorBody()?.string()}")
        } catch (e: Exception) {
            ApiResult.Error("An internal error occurred in updating profile: ${e.message}")
        }
    }

    override suspend fun addOpinion(
        targetUserId: Int,
        newUserOpinionDto: NewUserOpinionDto
    ): ApiResult<String> {
        return try {
            val response = apiWithAuthInterceptor.addOpinion(targetUserId, newUserOpinionDto)
            if(response.isSuccessful) {
                val data = response.body()!!.result
                ApiResult.Success(data)
            } else
                ApiResult.Error("Failed to add opinion: ${response.errorBody()?.string()}")
        } catch (e: Exception) {
            ApiResult.Error("An internal error occurred in adding opinion: ${e.message}")
        }
    }

    override suspend fun changePassword(newPasswordDto: NewPasswordDto): ApiResult<String> {
        return try {
            val response = apiWithAuthInterceptor.changePassword(newPasswordDto)
            if(response.isSuccessful) {
                val data = response.body()!!.result
                ApiResult.Success(data)
            } else
                ApiResult.Error("Failed to change password: ${response.errorBody()?.string()}")
        } catch (e: Exception) {
            ApiResult.Error("An internal error occurred in changing password: ${e.message}")
        }
    }
}