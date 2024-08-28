package com.example.flats4us.services

import com.example.flats4us.data.MyProfile

interface UserProfileDataSource {
    fun getUserProfile(): MyProfile
}
