package com.example.reviewercompose

import io.ktor.client.HttpClient

object ServiceLocator {
    private var _httpClient: HttpClient? = null
    val httpClient get() = _httpClient!!

    fun setHttpClient(client: HttpClient) {
        if (_httpClient == null) _httpClient = client
    }
}