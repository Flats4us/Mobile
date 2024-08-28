package com.example.flats4us.services

import com.example.flats4us.data.ApiResult
import com.example.flats4us.data.RentDecision
import com.example.flats4us.data.StudentForMatcher

interface MatcherDataSource {
    suspend fun getPotentialMatches(): ApiResult<List<StudentForMatcher>>

    suspend fun getExistingMatches(): ApiResult<List<StudentForMatcher>>

    suspend fun acceptPotentialMatch(studentId: Int, decision: RentDecision): ApiResult<String>
}