package com.example.flats4us21.data

import com.google.gson.annotations.SerializedName

data class SurveyQuestion(
    @SerializedName("id")
    val questionId : Int,
    @SerializedName("title")
    val type : QuestionType,
    @SerializedName("content")
    val content : String,
    @SerializedName("type_name")
    val responseType : ResponseType,
    @SerializedName("answers")
    val answers : List<@JvmSuppressWildcards Any?>
)
