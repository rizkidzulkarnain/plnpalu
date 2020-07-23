package com.rizkidzulkarnain.core.domain

/**
 * Created by Rizki Dzulkarnain on 6/27/2020.
 */
open class BaseApiResponse {

    companion object{
        const val GENERAL_ERROR = "GENERAL_ERROR"
        const val SERVER_ERROR = "SERVER_ERROR"
        const val NETWORK_ERROR = "NETWORK_ERROR"
    }

    fun <T : Any> getResult(data: T): Result<T> {
        return Result.Success(data)
    }
}