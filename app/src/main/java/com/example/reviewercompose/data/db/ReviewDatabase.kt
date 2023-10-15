package com.example.reviewercompose.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.reviewercompose.data.db.daos.UserDao
import com.example.reviewercompose.data.db.tables.ImageEntity
import com.example.reviewercompose.data.db.tables.ItemEntity
import com.example.reviewercompose.data.db.tables.ParagraphEntity
import com.example.reviewercompose.data.db.tables.ReviewEntity
import com.example.reviewercompose.data.db.tables.UserEntity

@Database(
    entities = [
        UserEntity::class,
        ItemEntity::class,
        ReviewEntity::class,
        ParagraphEntity::class,
        ImageEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class ReviewDatabase : RoomDatabase() {
    abstract val userDao: UserDao

    companion object {
        @Volatile
        private var INSTANCE: ReviewDatabase? = null
        private const val DATABASE_NAME = "REVIEWER_DB"

        fun getInstance(context: Context): ReviewDatabase {
            if (INSTANCE != null) return INSTANCE!!
            synchronized(this) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    ReviewDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                return INSTANCE!!
            }
        }
    }
}