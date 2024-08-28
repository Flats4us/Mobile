package com.example.flats4us.data

import com.google.gson.annotations.SerializedName

data class QuestionResponse(
    @SerializedName("id")
    val questionId : Int,
    @SerializedName("response")
    val answer : String
)
