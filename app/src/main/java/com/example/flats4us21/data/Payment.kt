package com.example.flats4us21.data

data class Payment(
    val paymentId: Int,
    val paymentPurpose: String,
    val amount: Int,
    val isPaid: Boolean,
    val createdDate: String,
    val paymentDate: String
)
