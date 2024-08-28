package com.example.flats4us.data

data class PropertyOpinion(
    val rentOpinionId: Int,
    val date: String,
    val rating: Int,
    val description: String,
    val sourceUserId: Int,
    val sourceUserName: String,
    val sourceUserProfilePicture: Image?
)
