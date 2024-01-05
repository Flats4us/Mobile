package com.example.flats4us21.services

import com.example.flats4us21.data.dto.NewUserDto
import com.example.flats4us21.serializer.UserSerializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiUserDataSource: UserDataSource{

    private const val baseUrl= "https://raw.githubusercontent.com"

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
        api.login(email, password)
    }

    override suspend fun register(user: NewUserDto) {
        api.registerUser(user)
    }
}