package com.example.reviewercompose.data.db.daos

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface ItemDao {
    @Insert
    suspend fun insert(item: ItemDao)
}