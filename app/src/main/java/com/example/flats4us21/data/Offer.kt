package com.example.flats4us21.data

data class Offer(
    val offerId: Int,
    val dateIssue: String,
    val status: String,
    val price: String,
    val description: String,
    val rentalPeriod: String,
    val interestedPeople: Int,
    val userRegulation: String?,
    val property: Property
)