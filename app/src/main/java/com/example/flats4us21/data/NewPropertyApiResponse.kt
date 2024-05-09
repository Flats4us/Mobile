package com.example.flats4us21.data

import com.google.gson.annotations.SerializedName

data class NewPropertyApiResponse<T>(
    @SerializedName("result")
    val result: T
)
