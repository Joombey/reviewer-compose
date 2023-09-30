package com.example.reviewercompose.data.db.tables

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "items",
    indices = [Index("id")]
)
data class ItemEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val model: String,
    val image: String
)