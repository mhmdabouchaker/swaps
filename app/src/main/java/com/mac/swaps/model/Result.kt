package com.mac.swaps.model

/**
 * Generic class for holding success response, error response and loading status
 */
data class Result<out T>(
    val data: T? = null,
    val error: String? = null,
    val loading: Boolean = false
) {

    companion object {
        fun <T> success(
            data: T?
        ): Result<T> {
            return Result(
                data = data,
            )
        }

        fun <T> error(
            message: String,
        ): Result<T> {
            return Result(
                error = message
            )
        }

        fun <T> loading(): Result<T> = Result(loading = true)
    }
}