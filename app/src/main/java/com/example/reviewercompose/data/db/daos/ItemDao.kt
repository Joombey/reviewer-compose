package com.example.reviewercompose.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.reviewercompose.data.db.tables.ItemEntity

@Dao
interface ItemDao {
    @Insert
    suspend fun insert(item: ItemEntity)
    @Query("select * from items where id = :id")
    suspend fun getById(id: String): ItemEntity
}