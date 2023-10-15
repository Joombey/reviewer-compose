package com.example.reviewercompose.data.db.daos

import androidx.room.Dao
import androidx.room.Query
import com.example.reviewercompose.data.db.tables.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<UserEntity>
}