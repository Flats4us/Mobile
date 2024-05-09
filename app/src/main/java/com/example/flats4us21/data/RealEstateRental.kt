package com.example.flats4us21.data

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class RealEstateRental(
    @SerializedName("roommatesEmails")
    val roommatesEmails: MutableList<String>,
    @SerializedName("startDate")
    val startDate: LocalDate,
    @SerializedName("duration")
    val duration: Int
)
