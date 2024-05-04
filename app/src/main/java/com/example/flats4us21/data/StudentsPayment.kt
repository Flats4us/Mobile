package com.example.flats4us21.data

import java.time.LocalDate

data class StudentsPayment(
    open val paymentId : Int,
    open val type : PaymentType,
    open val amount : Double,
    open val receiptOfTransferDate: LocalDate?,
    open val period : String,
    open val toDate : LocalDate,
    open val property : com.example.flats4us21.data.dto.Property
)
