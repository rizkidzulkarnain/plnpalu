package com.rizkidzulkarnain.core.domain

/**
 * Created by Rizki Dzulkarnain on 6/27/2020.
 */
sealed class Result<out T> {
    data class Success<out T>(val value: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>() {
        var code: String = ""
            private set

        var message: String = ""
            private set

        constructor(code: String? = null, message: String? = null) : this(Throwable(message)) {
            this.code = code.orEmpty()
            this.message = message.orEmpty()
        }
    }
}