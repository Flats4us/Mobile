package com.example.flats4us21.data

import java.time.LocalDate

data class StudentsPayment(
    val paymentId : Int,
    val type : PaymentType,
    val amount : Double,
    val receiptOfTransferDate: LocalDate?,
    val period : String,
    val toDate : LocalDate,
    val property : com.example.flats4us21.data.dto.Property
)
