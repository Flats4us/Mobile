package com.example.flats4us21.data

data class Offer(
    val dateIssue : String,
    val status : String,
    val price : Float,
    val description : String,
    val rentalPeriod : String,
    val interestedPeople : Int,
    val property : Property
)
