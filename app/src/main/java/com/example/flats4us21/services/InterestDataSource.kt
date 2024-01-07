package com.example.flats4us21.services

import com.example.flats4us21.data.Interest

interface InterestDataSource {

    suspend fun getInterests(): List<Interest>
}
