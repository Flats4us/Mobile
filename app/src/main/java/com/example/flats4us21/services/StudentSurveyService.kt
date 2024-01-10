package com.example.flats4us21.services

import android.util.Log
import com.example.flats4us21.adapters.deserializer.SurveyDeserializer
import com.example.flats4us21.data.QuestionResponse
import com.example.flats4us21.data.SurveyQuestion
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object StudentSurveyService {

    private const val baseUrl = "https://raw.githubusercontent.com/"

    val gson = GsonBuilder()
        .registerTypeAdapter(SurveyQuestion::class.java, SurveyDeserializer())
        .create()

    private val api = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(IStudentSurveyService::class.java)

    suspend fun getSurveyQuestion(surveyType: String): List<SurveyQuestion> {
        return api.getSurveyQuestions(surveyType)
    }

    fun postSurveyQuestions(list : List<QuestionResponse>){
        val gson = Gson()
        val jsonString = gson.toJson(list)
        Log.i("StudentSurveyService", "Json: $jsonString")
    }
}