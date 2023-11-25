package com.example.reviewercompose

import android.app.Application
import android.util.Log
import com.example.reviewercompose.data.api.OkHttpInterceptor
import com.example.reviewercompose.data.api.SerpApiRepository
import com.example.reviewercompose.data.api.SerpApiRepositoryImpl
import com.example.reviewercompose.data.repository.DataBaseRepository
import com.example.reviewercompose.data.repository.DataBaseRepositoryImpl
import com.example.reviewercompose.data.db.ReviewDatabase
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

class ReviewerApplication : Application() {
    private var _userRepository: DataBaseRepositoryImpl? = null
    private var _apiRepository: SerpApiRepository? = null
    override fun onCreate() {
        super.onCreate()
        val db = ReviewDatabase.getInstance(this)
        _userRepository = DataBaseRepositoryImpl(db)
        val httpClient = configureHttpClient()
        ServiceLocator.setHttpClient(httpClient)
        _apiRepository = SerpApiRepositoryImpl()
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun configureHttpClient(): HttpClient {
        return HttpClient(OkHttp) {
            engine { addInterceptor(OkHttpInterceptor()) }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.i("ktor", message)
                    }
                }
                level = LogLevel.ALL
            }
            expectSuccess = true
            install(ContentNegotiation) {
                json(Json {
                    isLenient = true
                    prettyPrint = true
                    namingStrategy = JsonNamingStrategy.SnakeCase
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    val userRepository: DataBaseRepository get() = _userRepository!!
    val apiRepository: SerpApiRepository get() = _apiRepository!!
}