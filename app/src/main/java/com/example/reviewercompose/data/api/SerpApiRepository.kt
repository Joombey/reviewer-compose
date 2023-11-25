package com.example.reviewercompose.data.api

interface SerpApiRepository {
    suspend fun getList(query: String): QueryResult<ShoppingResult>
}