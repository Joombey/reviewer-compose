package com.example.reviewercompose.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import com.example.reviewercompose.data.db.tables.ImageEntity

@Dao
interface ImageDao {
    @Insert
    suspend fun insertImage(imageEntity: ImageEntity)
}