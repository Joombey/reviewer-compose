package com.example.reviewercompose.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import com.example.reviewercompose.data.db.tables.ReviewEntity

@Dao
interface ReviewDao {

    @Insert
    suspend fun insert(review: ReviewEntity)
}