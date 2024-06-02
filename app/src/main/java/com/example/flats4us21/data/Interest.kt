package com.example.flats4us21.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Interest (
    @SerializedName("interestId")
    val interestId: Int,
    @SerializedName("name")
    val interestName: String
): Parcelable
