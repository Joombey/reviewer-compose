package com.example.reviewercompose.data.repository.db

import com.example.reviewercompose.data.entities.Paragraph
import com.example.reviewercompose.data.entities.Product
import com.example.reviewercompose.data.entities.User
import kotlinx.coroutines.flow.Flow

interface DataBaseRepository {
    suspend fun createUser(user: User, login: String, password: String)
    suspend fun createReview(
        user: User,
        product: Product,
        paragraphs: List<Paragraph>,
        imagePath: String? = null
    )

    val currentUser: Flow<User?>
}