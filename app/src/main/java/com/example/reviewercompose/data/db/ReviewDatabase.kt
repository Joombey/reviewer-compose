package com.example.reviewercompose.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.reviewercompose.data.db.daos.ImageDao
import com.example.reviewercompose.data.db.daos.ItemDao
import com.example.reviewercompose.data.db.daos.ParagraphDao
import com.example.reviewercompose.data.db.daos.ReviewDao
import com.example.reviewercompose.data.db.daos.UserDao
import com.example.reviewercompose.data.db.tables.ImageEntity
import com.example.reviewercompose.data.db.tables.ItemEntity
import com.example.reviewercompose.data.db.tables.ParagraphEntity
import com.example.reviewercompose.data.db.tables.ReviewEntity
import com.example.reviewercompose.data.db.tables.UserEntity
import com.example.reviewercompose.data.entities.User

@Database(
    entities = [
        UserEntity::class,
        ItemEntity::class,
        ReviewEntity::class,
        ParagraphEntity::class,
        ImageEntity::class,
    ],
    views = [
        User::class
    ],
    version = 4,
    exportSchema = false
)
abstract class ReviewDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val imageDao: ImageDao
    abstract val itemDao: ItemDao
    abstract val reviewDao: ReviewDao
    abstract val paragraphDao: ParagraphDao

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