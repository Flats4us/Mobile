package com.example.flats4us21.services

import com.example.flats4us21.data.StudentsPayment

interface StudentsPaymentDataSource {
    suspend fun getStudentsPayment( paymentId : Int) : StudentsPayment
}