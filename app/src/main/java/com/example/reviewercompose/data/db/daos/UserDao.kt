package com.example.reviewercompose.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.reviewercompose.data.db.tables.UserEntity
import com.example.reviewercompose.data.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<UserEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(entity: UserEntity)

    @Transaction
    @Query("SELECT * FROM User")
    fun currentUser(): Flow<User>

    @Transaction
    @Query("SELECT * FROM User")
    suspend fun getCurrentUser(): User
}