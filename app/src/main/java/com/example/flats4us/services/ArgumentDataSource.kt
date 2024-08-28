package com.example.flats4us.services

import com.example.flats4us.data.ApiResult
import com.example.flats4us.data.Argument
import com.example.flats4us.data.dto.NewArgumentDto

interface ArgumentDataSource {

    suspend fun getArguments(): ApiResult<List<Argument>>

    suspend fun addArgument(argument: NewArgumentDto): ApiResult<String>

    suspend fun ownerAcceptArgument(id: Int): ApiResult<String>

    suspend fun studentAcceptArgument(argumentId: Int): ApiResult<String>

    suspend fun askingForIntervention(id: Int): ApiResult<String>
}