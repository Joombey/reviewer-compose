package com.example.reviewercompose.data.db.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "users",
    indices = [Index("id")]
)
data class UserEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    @ColumnInfo("is_current")
    val isCurrent: Boolean,
    val name: String,
    @ColumnInfo("creation_time")
    val creationTime: Long,
    @ColumnInfo("last_online")
    val lastOnline: Long,
)
