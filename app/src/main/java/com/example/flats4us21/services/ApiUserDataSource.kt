package com.example.flats4us21.services

import com.example.flats4us21.data.dto.LoginRequest
import com.example.flats4us21.data.dto.NewUserDto
import com.example.flats4us21.serializer.UserSerializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiUserDataSource: UserDataSource{

    private const val baseUrl= "http://10.0.2.2:5166/"

    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(NewUserDto::class.java, UserSerializer())
        .create()

    private val api: UserService by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(UserService::class.java)
    }

    override suspend fun login(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)
        api.login(loginRequest)
    }


    override suspend fun register(user: NewUserDto) {
        api.registerUser(user)
    }
}