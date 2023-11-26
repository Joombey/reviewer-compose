package com.example.reviewercompose.data.repository.api

interface SerpApiRepository {
    suspend fun query(query: String): QueryResult
    suspend fun fetchBytes(url: String): ByteArray
}