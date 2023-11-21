package com.example.reviewercompose

import android.app.Application
import com.example.reviewercompose.data.DataBaseRepository
import com.example.reviewercompose.data.DataBaseRepositoryImpl
import com.example.reviewercompose.data.db.ReviewDatabase

class ReviewerApplication: Application() {
    private var _userRepository: DataBaseRepositoryImpl? = null
    override fun onCreate() {
        super.onCreate()
        val db = ReviewDatabase.getInstance(this)
        _userRepository = DataBaseRepositoryImpl(db)
    }

    val userRepository: DataBaseRepository get() = _userRepository!!
}