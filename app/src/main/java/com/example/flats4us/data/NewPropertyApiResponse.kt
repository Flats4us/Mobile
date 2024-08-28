package com.example.flats4us.data

import com.google.gson.annotations.SerializedName

data class NewPropertyApiResponse<T>(
    @SerializedName("result")
    val result: T
)
