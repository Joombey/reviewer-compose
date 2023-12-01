package com.example.reviewercompose

import android.app.Application
import android.util.Log
import com.example.reviewercompose.data.api.OkHttpInterceptor
import com.example.reviewercompose.data.repository.api.SerpApiRepository
import com.example.reviewercompose.data.repository.api.SerpApiRepositoryImpl
import com.example.reviewercompose.data.repository.db.DataBaseRepository
import com.example.reviewercompose.data.repository.db.DataBaseRepositoryImpl
import com.example.reviewercompose.data.db.ReviewDatabase
import com.example.reviewercompose.data.repository.storage.StorageRepository
import com.example.reviewercompose.data.repository.storage.StorageRepositoryImpl
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
    val userRepository: DataBaseRepository get() = _userRepository!!

    private var _apiRepository: SerpApiRepository? = null
    val apiRepository: SerpApiRepository get() = _apiRepository!!

    private var _storageRepository: StorageRepository? = null
    val storageRepository get() = _storageRepository!!

    override fun onCreate() {
        super.onCreate()
        val db = ReviewDatabase.getInstance(this)
        val httpClient = configureHttpClient()
        ServiceLocator.setHttpClient(httpClient)
        ServiceLocator.setCacheDir(cacheDir.path)
        ServiceLocator.setMediaDir(filesDir.absolutePath + "/media")
        _storageRepository = StorageRepositoryImpl(contentResolver)
        _userRepository = DataBaseRepositoryImpl(db, storageRepository)
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
}