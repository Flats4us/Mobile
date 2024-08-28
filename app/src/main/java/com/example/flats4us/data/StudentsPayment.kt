package com.example.flats4us.data

import java.time.LocalDate

data class StudentsPayment(
    val paymentId : Int,
    val type : PaymentType,
    val amount : Double,
    val receiptOfTransferDate: LocalDate?,
    val period : String,
    val toDate : LocalDate,
    val property : Property
)
