package com.example.flats4us.services

import com.example.flats4us.data.StudentsPayment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiStudentsPaymentDataSource : StudentsPaymentDataSource {
    private const val baseUrl = "https://raw.githubusercontent.com"

    val gson: Gson = GsonBuilder()
        .create()

    private val api: StudentsPaymentService by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(StudentsPaymentService::class.java)
    }


    override suspend fun getStudentsPayment(paymentId: Int): StudentsPayment {
        return api.getStudentsPayment(paymentId)
    }
}