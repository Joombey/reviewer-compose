package com.example.reviewercompose.data.api

import com.example.reviewercompose.BuildConfig
import com.example.reviewercompose.ServiceLocator
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class SerpApiRepositoryImpl : SerpApiRepository {
    private val client = ServiceLocator.httpClient
    override suspend fun getList(query: String): QueryResult<ShoppingResult> = try {
        client.get(BuildConfig.SHOPPING_URL) {
            url { parameter("q", query) }
        }.body<QueryResult.SerpApiShoppingResponse>()
    } catch (_: ClientRequestException) {
        QueryResult.ClientError
    } catch (_: ServerResponseException) {
        QueryResult.ServerError
    }
}

sealed class QueryResult<out T> {
    @Serializable
    data class SerpApiShoppingResponse(
        @SerialName("shopping_results")
        val shoppingResults: List<ShoppingResult>
    ) : QueryResult<ShoppingResult>()

    object ServerError : QueryResult<Nothing>()
    object ClientError : QueryResult<Nothing>()
}


@Serializable
data class ShoppingResult(
    val position: Int,
    val title: String? = null,
    val rating: String? = null,
    @SerialName("thumbnail")
    val imageUrl: String
)