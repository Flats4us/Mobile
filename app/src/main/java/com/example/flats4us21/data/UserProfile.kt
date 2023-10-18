package com.example.flats4us21.data

data class UserProfile(
    val firstName: String,
    val lastName: String,
    val email: String,
    val age: Int,
    val rating: Int,
    val bio: String,
    val profilePictureResId: Int  // Drawable resource ID for the profile picture
)
