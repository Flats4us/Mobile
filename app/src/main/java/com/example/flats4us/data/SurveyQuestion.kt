package com.example.flats4us.data

import com.google.gson.annotations.SerializedName

data class SurveyQuestion(
    @SerializedName("id")
    val id : Int,
    @SerializedName("name")
    val name : String,
    @SerializedName("trigger")
    val trigger : Boolean,
    @SerializedName("optional")
    val optional : Boolean,
    @SerializedName("typeName")
    val typeName : String,
    @SerializedName("answers")
    val answers : List<String>
)
