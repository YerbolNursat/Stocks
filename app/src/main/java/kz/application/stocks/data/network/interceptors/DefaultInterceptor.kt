package kz.application.stocks.data.network.interceptors

import kz.application.stocks.presentation.constants.Constants
import okhttp3.Interceptor
import okhttp3.Response

class DefaultInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("Content-Type", "application/json")
            .header("Authorization", Constants.FINHUB_API)
            .build()
        return chain.proceed(request)
    }

}