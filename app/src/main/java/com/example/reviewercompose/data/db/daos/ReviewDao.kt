package com.example.reviewercompose.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.reviewercompose.data.db.tables.ReviewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {

    @Insert
    suspend fun insert(review: ReviewEntity)

    @Query("SELECT * FROM REVIEWS WHERE user_id = :userId")
    fun getUserReviewList(userId: String): Flow<List<ReviewEntity>>

    @Query("SELECT * FROM REVIEWS")
    fun getAllReviews(): Flow<List<ReviewEntity>>
}