package com.example.reviewercompose.data.api

import android.util.Log
import com.example.reviewercompose.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class OkHttpInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val urlWithKey = chain.request().url
            .newBuilder()
            .addQueryParameter("api_key", BuildConfig.API_KEY)
            .build()
        val requestWithKey = chain.request()
            .newBuilder()
            .url(urlWithKey)
            .build()
        return chain.proceed(requestWithKey)
    }
}