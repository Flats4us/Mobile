package com.example.flats4us21.services

import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.NewTechnicalProblemsDto

interface TechnicalProblemsDataSource {

    suspend fun addTechnicalProblems(newTechnicalProblemsDto: NewTechnicalProblemsDto): ApiResult<String>
}