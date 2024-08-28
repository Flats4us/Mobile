package com.example.flats4us.services

import android.util.Log
import com.example.flats4us.DataStoreManager
import com.example.flats4us.URL
import com.example.flats4us.data.ApiResult
import com.example.flats4us.data.MyProfile
import com.example.flats4us.data.Profile
import com.example.flats4us.data.dto.LoginRequest
import com.example.flats4us.data.dto.LoginResponse
import com.example.flats4us.data.dto.NewOwnerDto
import com.example.flats4us.data.dto.NewPasswordDto
import com.example.flats4us.data.dto.NewStudentDto
import com.example.flats4us.data.dto.NewUserOpinionDto
import com.example.flats4us.data.dto.UpdateMyProfileDto
import com.example.flats4us.data.utils.Constants
import com.example.flats4us.interceptors.AuthInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.net.URLEncoder

private const val TAG = "ApiUserDataSource"

object ApiUserDataSource : UserDataSource {

    private val gson: Gson = GsonBuilder().create()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private fun createOkHttpClient(withAuth: Boolean): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(loggingInterceptor)
            if (withAuth) {
                addInterceptor(AuthInterceptor())
            }
        }.build()
    }

    private fun createRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private val api: UserService by lazy {
        createRetrofit(createOkHttpClient(withAuth = false)).create(UserService::class.java)
    }

    private val apiWithAuthInterceptor: UserService by lazy {
        createRetrofit(createOkHttpClient(withAuth = true)).create(UserService::class.java)
    }

    private fun isDataStoreInitialized(): Boolean {
        return DataStoreManager.isInitialized()
    }

    private suspend fun getUserService(): UserService {
        return if (!isDataStoreInitialized() || DataStoreManager.isUserTokenEmpty() || DataStoreManager.isTokenExpired()) {
            Log.i(TAG, "api")
            api
        } else {
            Log.i(TAG, "apiWithAuthInterceptor")
            apiWithAuthInterceptor
        }
    }

    override suspend fun login(email: String, password: String, token: String): ApiResult<LoginResponse?> {
        return try {
            val loginRequest = LoginRequest(email, password, token)
            val service = getUserService()
            val response = service.login(loginRequest)
            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    if (data.role != Constants.MODERATOR) {
                        Log.i(TAG, "data: $data")
                        DataStoreManager.saveUserData(data)
                        ApiResult.Success(data)
                    } else {
                        ApiResult.Error("moderator_login")
                    }
                } else {
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_failed_to_login")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_login")
        }
    }

    override suspend fun registerStudent(user: NewStudentDto): ApiResult<LoginResponse?> {
        return try {
            val service = getUserService()
            val response = service.registerStudent(user)
            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    DataStoreManager.saveUserData(data)
                    ApiResult.Success(data)
                } else {
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_failed_to_register")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_register")
        }
    }

    override suspend fun registerOwner(user: NewOwnerDto): ApiResult<LoginResponse?> {
        return try {
            val service = getUserService()
            val response = service.registerOwner(user)
            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    DataStoreManager.saveUserData(data)
                    ApiResult.Success(data)
                } else {
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_failed_to_register")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_register")
        }
    }

    override suspend fun checkEmail(email: String): ApiResult<Boolean> {
        return try {
            val service = getUserService()
            val encodedEmail = withContext(Dispatchers.IO) {
                URLEncoder.encode(email, "UTF-8")
            }
            Log.i(TAG, "Encoded email: $encodedEmail")
            val response = service.checkEmail(encodedEmail)
            if (response.isSuccessful) {
                val data = response.body()!!.result
                ApiResult.Success(data)
            } else {
                ApiResult.Error("error_failed_to_check_email")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_check_email")
        }
    }

    override suspend fun getProfile(): ApiResult<MyProfile> {
        return try {
            val response = apiWithAuthInterceptor.getProfile()
            if (response.isSuccessful) {
                val data = response.body()!!
                ApiResult.Success(data)
            } else {
                ApiResult.Error("error_failed_to_retrieve_profile")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_retrieve_profile")
        }
    }

    override suspend fun getProfile(id: Int): ApiResult<Profile> {
        return try {
            val service = getUserService()
            val response = service.getProfile(id)
            if (response.isSuccessful) {
                val data = response.body()!!
                ApiResult.Success(data)
            } else {
                ApiResult.Error("error_failed_to_retrieve_profile")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_retrieve_profile")
        }
    }

    override suspend fun sendPasswordResetLink(email: String): ApiResult<String> {
        return try {
            val service = getUserService()
            val response = service.sendPasswordResetLink(email)
            if (response.isSuccessful) {
                val data = response.body()!!.result
                ApiResult.Success("send_email_with_forgot_my_password_link")
            } else {
                ApiResult.Error("error_failed_to_send_password_reset_link")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_send_password_reset_link")
        }
    }

    override suspend fun updateMyProfile(updateMyProfileDto: UpdateMyProfileDto): ApiResult<String> {
        return try {
            val service = getUserService()
            val response = service.updateMyProfile(updateMyProfileDto)
            if (response.isSuccessful) {
                val data = response.body()?.string() ?: "Empty response"
                ApiResult.Success("success_updated_profile")
            } else {
                ApiResult.Error("error_failed_to_update_profile")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_update_profile")
        }
    }

    override suspend fun addUserFiles(
        profilePicture: File?,
        document: File?
    ): ApiResult<String> {
        return try{
            val profilePicturePart : MultipartBody.Part? = if ( profilePicture != null) {
                 MultipartBody.Part.createFormData("ProfilePicture", profilePicture.name, profilePicture.asRequestBody("image/jpeg".toMediaTypeOrNull()))
            } else {
                null
            }
            val documentPart : MultipartBody.Part? = if( document != null) {
                 MultipartBody.Part.createFormData("document", document.name, document.asRequestBody("image/jpeg".toMediaTypeOrNull()))
            } else {
                null
            }
            val response = apiWithAuthInterceptor.addUserFiles(profilePicturePart, documentPart)
            if (response.isSuccessful) {
                val data = response.body()!!.result
                ApiResult.Success("success_added_files")
            } else {
                ApiResult.Error("error_failed_to_add_files")
            }
        } catch(e: Exception){
        ApiResult.Error("internal_error_failed_to_add_files")
        }
    }

    override suspend fun addOpinion(targetUserId: Int, newUserOpinionDto: NewUserOpinionDto): ApiResult<String> {
        return try {
            val service = getUserService()
            val response = service.addOpinion(targetUserId, newUserOpinionDto)
            if (response.isSuccessful) {
                val data = response.body()!!.result
                ApiResult.Success("success_added_opinion")
            } else {
                ApiResult.Error("error_failed_to_add_opinion")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_add_opinion")
        }
    }

    override suspend fun changePassword(newPasswordDto: NewPasswordDto): ApiResult<String> {
        return try {
            val service = getUserService()
            val response = service.changePassword(newPasswordDto)
            if (response.isSuccessful) {
                val data = response.body()!!.result
                ApiResult.Success("success_changed_password")
            } else {
                ApiResult.Error("error_failed_to_change_password")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_change_password")
        }
    }

    override suspend fun changeEmail(email: String): ApiResult<String> {
        return try {
            val service = getUserService()
            val encodedEmail = withContext(Dispatchers.IO) {
                URLEncoder.encode(email, "UTF-8")
            }
            Log.i(TAG, "Encoded email: $encodedEmail")
            val jsonObject = JSONObject()
            jsonObject.put("email", email)

            val requestBody: RequestBody = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())

            val response = service.changeEmail(requestBody)
            if (response.isSuccessful) {
                val data = response.body()!!.string()
                ApiResult.Success("success_changed_email")
            } else {
                ApiResult.Error("error_failed_to_change_email")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_change_email")
        }
    }

    override suspend fun logout(): ApiResult<String> {
        return try {
            val service = getUserService()
            val response = service.logout()
            if (response.isSuccessful) {
                val data = response.body()
                ApiResult.Success("")
            } else {
                ApiResult.Error("")
            }
        } catch (e: Exception) {
            ApiResult.Error("")
        }
    }
}
