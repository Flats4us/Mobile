package com.example.flats4us21.services

import com.example.flats4us21.data.Argument
import com.example.flats4us21.data.NewPropertyApiResponse
import com.example.flats4us21.data.dto.NewArgumentDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ArgumentService {

    @GET("api/arguments")
    suspend fun getArguments(): Response<List<Argument>>


    @POST("api/arguments")
    suspend fun addArgument(@Body argument: NewArgumentDto): Response<NewPropertyApiResponse<String>>

    @PUT("api/arguments/ownerAcceptArgument")
    suspend fun ownerAcceptArgument(@Query("id") id: Int): Response<NewPropertyApiResponse<String>>

    @PUT("api/arguments/studentAcceptArgument")
    suspend fun studentAcceptArgument(@Query("argumentId") argumentId: Int): Response<NewPropertyApiResponse<String>>

    @PUT("api/arguments/askingForIntervention/{id}")
    suspend fun askingForIntervention(@Query("id") id: Int): Response<NewPropertyApiResponse<String>>
}