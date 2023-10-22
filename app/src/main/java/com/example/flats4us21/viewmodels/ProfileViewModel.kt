package com.example.flats4us21.viewmodels

import androidx.lifecycle.ViewModel
import com.example.flats4us21.data.UserProfile
import com.example.flats4us21.services.UserProfileDataSource
import com.example.flats4us21.services.HardcodedUserProfileDataSource

class ProfileViewModel : ViewModel() {
    private val profileRepository: UserProfileDataSource = HardcodedUserProfileDataSource()

    fun getUserProfile(): UserProfile {
        return profileRepository.getUserProfile()
    }
}
