package com.example.flats4us21.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Meeting(
    val meetingId: Int,
    val date: String,
    val place: String,
    val reason: String,
    val offerId: Int,
    val studentAcceptDate: String?,
    val ownerAcceptDate: String?,
    val needsAction: Boolean
): Parcelable
