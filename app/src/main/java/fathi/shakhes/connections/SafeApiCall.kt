package fathi.shakhes.connections

import android.app.Activity
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException


suspend fun <T> safeApiCall(apiCall: suspend () -> T): ResultWrapper<T> {
    return safeApiCall(apiCall, 0)
}

suspend fun <T> safeApiCall(apiCall: suspend () -> T, depth: Int): ResultWrapper<T> {
    return withContext(Dispatchers.IO) {
        try {
            ResultWrapper.Success(apiCall.invoke(), null)
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> ResultWrapper.NetworkError(apiCall)
                is HttpException -> {
                    getAppropriateError(throwable, apiCall, depth)
                }
                is KotlinNullPointerException -> {
                    ResultWrapper.KotlinNpeError(null)
                }
                else -> {
                    ResultWrapper.GenericError(null, null, apiCall)
                }
            }
        }
    }
}

private suspend fun <T> retryUnauthorizedError(apiCall: suspend () -> T, depth: Int): ResultWrapper<T> {
    return if (depth < 3) {
        try {
            // TODO: 04.09.21 getNewToken
//            ApiHelper.getNewToken()
        } catch (exception: HttpException) {
            if (exception.code() == 401) {
                return ResultWrapper.UnauthorizedError(apiCall)
            }
        }
        safeApiCall(apiCall, depth + 1)
    } else ResultWrapper.UnauthorizedError(apiCall)
}

private suspend fun <T> getAppropriateError(throwable: HttpException, apiCall: suspend () -> T, depth: Int): ResultWrapper<T> {
    return when (throwable.code()) {
        400 -> ResultWrapper.BadRequestError(apiCall, throwable)
        401 -> retryUnauthorizedError(apiCall, depth)
        403 -> ResultWrapper.ForbiddenError(apiCall)
        404 -> ResultWrapper.RequestNotFoundError(apiCall)
        else -> ResultWrapper.GenericError(throwable.code(), throwable.message(), apiCall)
    }
}

fun <T> handleErrors(activity: Activity?, result: ResultWrapper<T>) {
    activity ?: return
    when (result) {
        is ResultWrapper.Success -> {
            // Your code must not enter this part if result is ResultWrapper.Success
            return
        }
        is ResultWrapper.UnauthorizedError -> {
            logoutUser(activity)
        }
        else -> {
            if (activity.isFinishing) return

            // TODO: 04.09.21  showNetworkIssue
        }
    }
}

private fun logoutUser(context: Context) {
    // TODO: 04.09.21 Log out user
}