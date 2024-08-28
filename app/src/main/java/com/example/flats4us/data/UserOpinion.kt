package com.example.flats4us.data

data class UserOpinion(
    val userOpinionId: Int,
    val date: String,
    val rating: Int,
    val description: String,
    val sourceUserId: Int,
    val sourceUserName: String,
    val sourceUserProfilePicture: Image
)
