package fathi.shakhes.connections

import retrofit2.HttpException

sealed class ResultWrapper<out T>(open val apiCall: (suspend () -> T)?) {
    data class Success<out T>(val value: T, override val apiCall: (suspend () -> T)?) : ResultWrapper<T>(apiCall)
    data class GenericError<out T>(
        val code: Int? = null,
        val error: String? = null,
        override val apiCall: (suspend () -> T)?
    ) : ResultWrapper<T>(apiCall)

    data class NetworkError<out T>(override val apiCall: (suspend () -> T)?) : ResultWrapper<T>(apiCall)
    data class UnauthorizedError<out T>(override val apiCall: (suspend () -> T)?) : ResultWrapper<T>(apiCall)
    data class ForbiddenError<out T>(override val apiCall: (suspend () -> T)?) : ResultWrapper<T>(apiCall)
    data class BadRequestError<out T>(override val apiCall: suspend () -> T, val throwable: HttpException) : ResultWrapper<T>(apiCall)
    data class RequestNotFoundError<out T>(override val apiCall: (suspend () -> T)?) : ResultWrapper<T>(apiCall)
    data class KotlinNpeError<out T>(override val apiCall: (suspend () -> T)?) : ResultWrapper<T>(apiCall)
}
