package com.example.flats4us21.data.dto


import com.google.gson.annotations.SerializedName

data class NewMeetingDto(
    @SerializedName("date")
    val date: String,
    @SerializedName("place")
    val place: String,
    @SerializedName("reason")
    val reason: String,
    @SerializedName("offerId")
    val offerId: Int
)