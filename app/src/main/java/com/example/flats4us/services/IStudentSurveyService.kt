package com.example.flats4us.services

import com.example.flats4us.data.SurveyQuestion
import retrofit2.Response
import retrofit2.http.GET

interface IStudentSurveyService {

    @GET("/api/surveys/template/student")
    suspend fun getStudentSurveyQuestions (): Response<List<SurveyQuestion>>

    @GET("/api/surveys/template/owner")
    suspend fun getOwnerSurveyQuestions (): Response<List<SurveyQuestion>>
}