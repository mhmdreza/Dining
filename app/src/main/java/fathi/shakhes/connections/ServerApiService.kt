package fathi.shakhes.connections

import retrofit2.http.Body
import retrofit2.http.POST

interface ServerApiService {
    @POST("api/v1/users/")
    suspend fun requestVerificationCode(
        @Body params: HashMap<String, String>
    )

}