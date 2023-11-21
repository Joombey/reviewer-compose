package com.example.reviewercompose.data.entities

import androidx.room.DatabaseView

@DatabaseView(
    "select users.id, users.name, images.uri as iconUri from users " +
            "inner join images on users.id = images.user_id and users.is_current = 1"
)
data class User(
    val id: String,
    val name: String,
    val iconUri: String
)