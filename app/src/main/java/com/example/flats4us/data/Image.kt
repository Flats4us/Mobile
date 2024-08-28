package com.example.flats4us.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    val name: String,
    val path: String
) : Parcelable
