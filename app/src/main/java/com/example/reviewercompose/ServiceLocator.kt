package com.example.reviewercompose

import io.ktor.client.HttpClient

object ServiceLocator {
    private var _httpClient: HttpClient? = null
    val httpClient get() = _httpClient!!

    fun setHttpClient(client: HttpClient) {
        if (_httpClient == null) _httpClient = client
    }

    private var _cacheDir: String? = null
    val cacheDir get() = _cacheDir!!

    fun setCacheDir(dir: String) {
        if (_cacheDir == null) _cacheDir = dir
    }

    private var _mediaDir: String? = null
    val mediaDir get() = _mediaDir!!

    fun setMediaDir(dir: String) {
        if (_mediaDir == null) _mediaDir = dir
    }
}