package com.example.reviewercompose.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
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
    suspend fun getCurrentUser(): User?

    @Query("UPDATE users SET is_current = :isCurrent WHERE id = :id")
    suspend fun updateUser(isCurrent: Boolean, id: String)

    @Transaction
    suspend fun switchUser(idToUpdate: String, idToSwitch: String) {
        updateUser(false, idToUpdate)
        updateUser(true, idToSwitch)
    }

    @Query("SELECT * FROM users where login = :login and password = :password LIMIT 1")
    suspend fun getUsersByLoginAndPassword(login: String, password: String): UserEntity?
}