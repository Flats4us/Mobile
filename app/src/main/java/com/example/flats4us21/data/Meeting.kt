package com.example.flats4us21.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.time.LocalDateTime

@Parcelize
data class Meeting(
    val date : LocalDateTime,
    val reason : String,
    val student : @RawValue Student,
    val offer : @RawValue Offer,
    val status : MeetingStatus
): Parcelable
