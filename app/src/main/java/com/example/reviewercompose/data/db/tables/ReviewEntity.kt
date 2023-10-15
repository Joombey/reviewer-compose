package com.example.reviewercompose.data.db.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "reviews",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            deferred = true
        ),
        ForeignKey(
            entity = ItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["item_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            deferred = true
        )
    ],
//    primaryKeys = ["id", "user_id"],
    indices = [
        Index(value = ["item_id", "id"]),
        Index(value = ["user_id"], unique = true)
    ]
)
data class ReviewEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    @ColumnInfo("creation_date")
    val creationDate: Long,
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "item_id")
    val itemId: String
)
