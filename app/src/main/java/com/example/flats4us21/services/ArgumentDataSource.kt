package com.example.flats4us21.services

import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.dto.NewArgumentDto

interface ArgumentDataSource {
    suspend fun addArgument(argument: NewArgumentDto): ApiResult<String>

    suspend fun ownerAcceptArgument(id: Int): ApiResult<String>

    suspend fun studentAcceptArgument(argumentId: Int): ApiResult<String>

    suspend fun askingForIntervention(id: Int): ApiResult<String>
}