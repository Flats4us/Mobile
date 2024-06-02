package com.example.flats4us21.data

data class PropertyOpinion(
    val rentOpinionId: Int,
    val date: String,
    val rating: Int,
    val description: String,
    val sourceUserName: String,
    val sourceUserProfilePicture: Image?
)
