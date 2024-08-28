package com.example.flats4us.services

import com.example.flats4us.data.ApiResult
import com.example.flats4us.data.NewTechnicalProblemsDto

interface TechnicalProblemsDataSource {

    suspend fun addTechnicalProblems(newTechnicalProblemsDto: NewTechnicalProblemsDto): ApiResult<String>
}