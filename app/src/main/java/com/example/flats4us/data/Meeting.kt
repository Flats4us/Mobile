package com.example.flats4us.data

data class Meeting(
    val meetingId: Int,
    val date: String,
    val place: String,
    val reason: String,
    val offerId: Int,
    val studentAcceptDate: String?,
    val ownerAcceptDate: String?,
    val needsAction: Boolean,
    val user : UserShortData
)
