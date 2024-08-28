package com.example.flats4us.services

import com.example.flats4us.data.StudentsPayment
import retrofit2.http.GET
import retrofit2.http.Path

interface StudentsPaymentService {
    @GET("/s22677/JSON-data-example/blob/main/StudentsPayment/{id}/StudentPayment.json")
    suspend fun getStudentsPayment(@Path("id") paymentId: Int): StudentsPayment
}