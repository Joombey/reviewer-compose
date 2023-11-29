package com.example.reviewercompose.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import com.example.reviewercompose.data.db.tables.ParagraphEntity

@Dao
interface ParagraphDao {
    @Insert
    suspend fun insert(paragraphEntity: ParagraphEntity)
}