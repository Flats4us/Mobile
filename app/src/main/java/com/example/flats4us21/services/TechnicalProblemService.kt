package com.example.flats4us21.services

import com.example.flats4us21.data.NewTechnicalProblemsDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TechnicalProblemService {

    @POST("api/technical-problems")
    suspend fun addTechnicalProblems(@Body newTechnicalProblemsDto: NewTechnicalProblemsDto): Response<Void>

}