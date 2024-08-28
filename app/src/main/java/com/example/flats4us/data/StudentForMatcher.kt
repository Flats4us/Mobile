package com.example.flats4us.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StudentForMatcher(
    val userId: Int,
    val name: String,
    val age: Int,
    val interest: List<Interest>?,
    val university: String,
    val profilePicture: Image?
) : Parcelable