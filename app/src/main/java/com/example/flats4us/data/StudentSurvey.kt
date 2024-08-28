package com.example.flats4us.data

import com.google.gson.annotations.SerializedName

data class StudentSurvey(
    @SerializedName("animals")
    val animals: Boolean,
    @SerializedName("city")
    val city: String,
    @SerializedName("lookingForRoommate")
    val lookingForRoommate: Boolean,
    @SerializedName("maxNumberOfRoommates")
    val maxNumberOfRoommates: Int,
    @SerializedName("maxRoommateAge")
    val maxRoommateAge: Int,
    @SerializedName("minRoommateAge")
    val minRoommateAge: Int,
    @SerializedName("party")
    val party: Int,
    @SerializedName("roommateGender")
    val roommateGender: Int,
    @SerializedName("smoking")
    val smoking: Boolean,
    @SerializedName("sociability")
    val sociability: Boolean,
    @SerializedName("tidiness")
    val tidiness: Int,
    @SerializedName("vegan")
    val vegan: Boolean
)
