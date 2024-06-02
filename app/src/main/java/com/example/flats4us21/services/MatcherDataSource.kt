package com.example.flats4us21.services

import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.RentDecision
import com.example.flats4us21.data.StudentForMatcher

interface MatcherDataSource {
    suspend fun getPotentialMatches(): ApiResult<List<StudentForMatcher>>

    suspend fun getExistingMatches(): ApiResult<List<StudentForMatcher>>

    suspend fun addNewMatcher(studentId: Int, decision: RentDecision): ApiResult<String>
}