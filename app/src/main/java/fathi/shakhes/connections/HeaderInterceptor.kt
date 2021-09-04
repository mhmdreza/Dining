package fathi.shakhes.connections

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        // TODO: 04.09.21 Add access token to API headers
//        if (SharedData.accessToken.isNullOrEmpty().not())
//            requestBuilder.addHeader("Authorization", "Bearer ${SharedData.accessToken}")

        return chain.proceed(requestBuilder.build())
    }
}