package com.example.flats4us21.services

import android.util.Log
import com.example.flats4us21.data.QuestionResponse
import com.example.flats4us21.data.SurveyQuestion
import com.example.flats4us21.adapters.deserializer.SurveyDeserializer
import com.example.flats4us21.viewmodels.MainViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StudentSurveyService(private val viewModel: MainViewModel) {

    private  val BASE_URL = "https://raw.githubusercontent.com/"

    fun getSurveyQuestion() {
        val gson = GsonBuilder()
            .registerTypeAdapter(SurveyQuestion::class.java, SurveyDeserializer())
            .create()

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val retrofitData = retrofitBuilder.create(IStudentSurveyService::class.java)

        retrofitData.getSurveyQuestions().enqueue(object : Callback<List<SurveyQuestion>?> {
            override fun onResponse(
                call: Call<List<SurveyQuestion>?>,
                response: Response<List<SurveyQuestion>?>
            ) {
                if (response.isSuccessful) {
                    viewModel.setQuestions(response.body()!!)
                } else {
                    Log.e("StudentSurveyService", "Failed to get survey questions: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<SurveyQuestion>?>, t: Throwable) {
                Log.e("StudentSurveyService", "Failed to get survey questions", t)
            }
        })
    }

    fun postSurveyQuestions(list : List<QuestionResponse>){
        val gson = Gson()
        val jsonString = gson.toJson(list)
        Log.i("StudentSurveyService", "Json: $jsonString")
    }
}