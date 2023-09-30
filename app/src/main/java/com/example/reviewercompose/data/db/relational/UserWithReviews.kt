package com.example.reviewercompose.data.db.relational

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.reviewercompose.data.db.tables.ReviewEntity
import com.example.reviewercompose.data.db.tables.UserEntity

data class UserWithReviews(
    @Embedded
    val user: UserEntity,
    @Relation(
        entity = UserEntity::class,
        parentColumn = "id",
        entityColumn = "user_id"
    )
    val review: ReviewEntity
)