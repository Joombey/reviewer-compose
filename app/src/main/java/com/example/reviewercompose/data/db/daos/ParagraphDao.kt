package com.example.reviewercompose.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.reviewercompose.data.db.tables.ParagraphEntity

@Dao
interface ParagraphDao {
    @Insert
    suspend fun insert(paragraphEntity: ParagraphEntity)

    @Query("select * from paragraphs where id = :id")
    suspend fun getById(id: String): List<ParagraphEntity>

    @Query("select * from paragraphs where review_id = :id")
    suspend fun getByReviewId(id: String): List<ParagraphEntity>
}