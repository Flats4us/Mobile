package com.example.flats4us21.services

import com.example.flats4us21.data.QuestionResponse
import com.example.flats4us21.data.SurveyQuestion
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface IStudentSurveyService {

    @GET("s22677/JSON-data-example/main/OWNER/JSON.json")
    fun getSurveyQuestions (): Call<List<SurveyQuestion>>

    @POST("endpoint/path")
    suspend fun sendQuestionResponses(@Body questionResponses: List<QuestionResponse>): Response<Void>


}