package com.example.flats4us.services

import com.example.flats4us.data.NewPropertyApiResponse
import com.example.flats4us.data.RentDecision
import com.example.flats4us.data.StudentForMatcher
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MatcherService {

    @GET("api/matcher/potential-by-id")
    suspend fun getPotentialMatches(): Response<List<StudentForMatcher>>


    @GET("api/matcher/existing-by-id")
    suspend fun getExistingMatches(): Response<List<StudentForMatcher>>


    @POST("api/matcher/accept/students/{studentId}")
    suspend fun addNewMatch(@Path("studentId") studentId: Int, @Body decision: RentDecision): Response<NewPropertyApiResponse<String>>


}