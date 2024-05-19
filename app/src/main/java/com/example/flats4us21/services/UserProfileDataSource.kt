package com.example.flats4us21.services

import com.example.flats4us21.data.MyProfile

interface UserProfileDataSource {
    fun getUserProfile(): MyProfile
}
