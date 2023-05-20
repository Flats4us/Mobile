package com.example.flats4us21.services

import com.example.flats4us21.data.SurveyQuestion
import retrofit2.Call
import retrofit2.http.GET

interface IStudentSurveyService {

    @GET("s22677/JSON-data-example/main/OWNER/JSON.json")
    fun getSurveyQuestions (): Call<List<SurveyQuestion>>
}