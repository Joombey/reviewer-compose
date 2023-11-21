package com.example.reviewercompose.data

import com.example.reviewercompose.data.entities.User
import kotlinx.coroutines.flow.Flow

interface DataBaseRepository {

    suspend fun createUser(user: User, login: String, password: String)
    val currentUser: Flow<User?>
}