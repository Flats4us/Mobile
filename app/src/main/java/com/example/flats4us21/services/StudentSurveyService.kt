package com.example.flats4us21.services

import android.util.Log
import com.example.flats4us21.data.SurveyQuestion
import com.example.flats4us21.viewmodels.MainViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StudentSurveyService(private val viewModel: MainViewModel) {

    private  val BASE_URL = "https://raw.githubusercontent.com/"

    fun getClient() {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
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
}