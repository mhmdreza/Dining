package fathi.shakhes.connections

import fathi.shakhes.connections.RetrofitBuilder.serverApiService

object ApiHelper {

    suspend fun requestVerificationCode(phoneNumber: String): ResultWrapper<Unit> {
        return safeApiCall {
            serverApiService.requestVerificationCode(
                hashMapOf(
                    "phone" to phoneNumber
                )
            )
        }
    }
}