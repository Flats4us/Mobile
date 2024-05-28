package com.example.flats4us21.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class Meeting(
    val date : String,
    val place : String,
    val reason : String,
    val offerId : Int
): Parcelable
