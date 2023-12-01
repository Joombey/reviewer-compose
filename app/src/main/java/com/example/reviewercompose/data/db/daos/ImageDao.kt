package com.example.reviewercompose.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.reviewercompose.data.db.tables.ImageEntity
import com.example.reviewercompose.data.db.tables.ParagraphEntity
import com.example.reviewercompose.data.entities.Paragraph

@Dao
interface ImageDao {
    @Insert
    suspend fun insertImage(imageEntity: ImageEntity)

    @Query("select uri from images where paragraph_id = :id")
    suspend fun getByParagraphId(id: String): List<String>
}