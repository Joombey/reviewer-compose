package com.example.reviewercompose.data.repository.db

import com.example.reviewercompose.data.entities.Paragraph
import com.example.reviewercompose.data.entities.Product
import com.example.reviewercompose.data.entities.Review
import com.example.reviewercompose.data.entities.User
import kotlinx.coroutines.flow.Flow

interface DataBaseRepository {
    suspend fun createUser(user: User, login: String, password: String)
    suspend fun getUserIdByLoginPass(login: String, password: String): String?

    suspend fun createReview(
        user: User,
        product: Product,
        title: String,
        paragraphs: List<Paragraph>,
        imagePath: String? = null
    )
    fun getUserReviewFor(userId: String): Flow<List<Review>>
    fun getAllReviews(): Flow<List<Review>>

    suspend fun getReviewByID(id: String): Review
    suspend fun switchUser(login: String, password: String)
    suspend fun exit()

    val currentUser: Flow<User?>
}