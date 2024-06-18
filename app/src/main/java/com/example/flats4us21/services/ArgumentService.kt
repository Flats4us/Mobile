package com.example.flats4us21.services

import com.example.flats4us21.data.Argument
import com.example.flats4us21.data.NewPropertyApiResponse
import com.example.flats4us21.data.dto.NewArgumentDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ArgumentService {

    @GET("api/arguments")
    suspend fun getArguments(): Response<List<Argument>>


    @POST("api/arguments")
    suspend fun addArgument(@Body argument: NewArgumentDto): Response<NewPropertyApiResponse<String>>

    @PUT("api/arguments/{argumentId}/owner-accept")
    suspend fun ownerAcceptArgument(@Path("argumentId") argumentId: Int): Response<NewPropertyApiResponse<String>>

    @PUT("api/arguments/{argumentId}/student-accept")
    suspend fun studentAcceptArgument(@Path("argumentId") argumentId: Int): Response<NewPropertyApiResponse<String>>

    @PUT("api/arguments/{argumentId}/asking-for-intervention")
    suspend fun askingForIntervention(@Path("argumentId") argumentId: Int): Response<NewPropertyApiResponse<String>>
}