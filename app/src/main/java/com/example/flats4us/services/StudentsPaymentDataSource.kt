package com.example.flats4us.services

import com.example.flats4us.data.StudentsPayment

interface StudentsPaymentDataSource {
    suspend fun getStudentsPayment( paymentId : Int) : StudentsPayment
}