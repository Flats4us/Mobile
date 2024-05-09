package com.example.flats4us21.services

import com.example.flats4us21.R
import com.example.flats4us21.data.UserProfile

class HardcodedUserProfileDataSource : UserProfileDataSource {
    override fun getUserProfile(): UserProfile {
        return UserProfile(
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            age = 28,
            rating = 4,
            bio = "Fajny ziomal szuka fajnego ziomala",
            profilePictureResId = R.drawable.baseline_person_24
        )
    }
}
