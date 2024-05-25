package com.example.flats4us21.services

import com.example.flats4us21.data.QuestionResponse
import com.example.flats4us21.data.SurveyQuestion
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface IStudentSurveyService {

    @GET("/api/surveys/template/student")
    suspend fun getStudentSurveyQuestions (): Response<List<SurveyQuestion>>

    @GET("/api/surveys/template/owner")
    suspend fun getOwnerSurveyQuestions (): Response<List<SurveyQuestion>>
}