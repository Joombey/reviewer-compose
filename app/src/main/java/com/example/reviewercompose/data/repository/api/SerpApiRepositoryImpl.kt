package com.example.reviewercompose.data.repository.api

import com.example.reviewercompose.BuildConfig
import com.example.reviewercompose.ServiceLocator
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsChannel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.nio.ByteBuffer

class SerpApiRepositoryImpl : SerpApiRepository {
    private val client = ServiceLocator.httpClient
    private val cacheDir: String = ServiceLocator.cacheDir
    override suspend fun query(query: String): QueryResult = try {
        client.get(BuildConfig.SHOPPING_URL) {
            url { parameter("q", query) }
        }.body<QueryResult.SerpApiShoppingResponse>()
    } catch (_: ClientRequestException) {
        QueryResult.ClientError
    } catch (_: ServerResponseException) {
        QueryResult.ServerError
    }

    override suspend fun fetchBytes(url: String): ByteArray {
        val byteBuffer: ByteBuffer = ByteBuffer.allocate(50 * 1024)
        client.get(url).bodyAsChannel().readFully(byteBuffer)
        return byteBuffer.array()
    }
}

sealed class QueryResult {
    @Serializable
    data class SerpApiShoppingResponse(
        @SerialName("shopping_results")
        val shoppingResults: List<ShoppingResult>
    ) : QueryResult()

    object ServerError : QueryResult()
    object ClientError : QueryResult()
}


@Serializable
data class ShoppingResult(
    val position: Int,
    @SerialName("product_id")
    val productId: Int,
    val title: String,
    val rating: String? = null,
    @SerialName("thumbnail")
    val imageUrl: String
)