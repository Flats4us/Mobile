package com.example.flats4us21.services

import com.example.flats4us21.data.UserProfile

interface UserProfileDataSource {
    fun getUserProfile(): UserProfile
}
