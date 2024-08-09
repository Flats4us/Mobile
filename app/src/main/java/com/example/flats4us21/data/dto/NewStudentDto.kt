package com.example.flats4us21.data.dto


import com.google.gson.annotations.SerializedName

data class NewStudentDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("surname")
    val surname: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("fcmToken")
    val fcmToken: String,
    @SerializedName("documentType")
    val documentType: Int,
    @SerializedName("documentExpireDate")
    val documentExpireDate: String,
    @SerializedName("birthDate")
    val birthDate: String,
    @SerializedName("studentNumber")
    val studentNumber: String,
    @SerializedName("university")
    val university: String,
    @SerializedName("links")
    val links: List<String>,
    @SerializedName("party")
    val party: Int,
    @SerializedName("tidiness")
    val tidiness: Int,
    @SerializedName("smoking")
    val smoking: Boolean,
    @SerializedName("sociability")
    val sociability: Boolean,
    @SerializedName("animals")
    val animals: Boolean,
    @SerializedName("vegan")
    val vegan: Boolean,
    @SerializedName("lookingForRoommate")
    val lookingForRoommate: Boolean,
    @SerializedName("maxNumberOfRoommates")
    val maxNumberOfRoommates: Int,
    @SerializedName("roommateGender")
    val roommateGender: Int,
    @SerializedName("minRoommateAge")
    val minRoommateAge: Int,
    @SerializedName("maxRoommateAge")
    val maxRoommateAge: Int,
    @SerializedName("city")
    val city: String,
    @SerializedName("interestIds")
    val interestIds: List<Int>
)