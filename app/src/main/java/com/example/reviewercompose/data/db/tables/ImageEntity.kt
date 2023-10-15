package com.example.reviewercompose.data.db.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "images",
    indices = [Index("id", "user_id", "item_id", "paragraph_id")],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.SET_NULL,
            onUpdate = ForeignKey.CASCADE,
            deferred = true
        ),
        ForeignKey(
            entity = ItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["item_id"],
            onDelete = ForeignKey.SET_NULL,
            onUpdate = ForeignKey.CASCADE,
            deferred = true
        ),
        ForeignKey(
            entity = ParagraphEntity::class,
            parentColumns = ["id"],
            childColumns = ["paragraph_id"],
            onDelete = ForeignKey.SET_NULL,
            onUpdate = ForeignKey.CASCADE,
            deferred = true
        )
    ]
)
data class ImageEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val uri: String,
    @ColumnInfo("user_id")
    val userId: String?,
    @ColumnInfo("item_id")
    val itemId: String?,
    @ColumnInfo("paragraph_id")
    val paragraphId: String?
)