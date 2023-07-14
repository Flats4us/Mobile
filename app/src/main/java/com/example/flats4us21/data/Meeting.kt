package com.example.flats4us21.data

import java.time.LocalDateTime

data class Meeting(
    val date : LocalDateTime,
    val reason : String,
    val student : Student,
    val offer : Offer
) : java.io.Serializable
