package com.example.reviewercompose.data.db.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "paragraphs",
    indices = [Index("id", "review_id")],
    foreignKeys = [
        ForeignKey(
            entity = ReviewEntity::class,
            parentColumns = ["id"],
            childColumns = ["review_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            deferred = true
        )
    ]
)
data class ParagraphEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val title: String?,
    val text: String,
    @ColumnInfo("review_id")
    val reviewId: String
)