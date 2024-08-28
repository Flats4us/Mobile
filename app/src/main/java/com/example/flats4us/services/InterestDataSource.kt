package com.example.flats4us.services

import com.example.flats4us.data.ApiResult
import com.example.flats4us.data.Interest

interface InterestDataSource {

    suspend fun getInterests(): ApiResult<List<Interest>>
}
