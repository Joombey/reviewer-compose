package com.example.reviewercompose

import android.app.Application
import com.example.reviewercompose.data.db.ReviewDatabase

class ReviewerApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }

    private fun initRepository() {
        val db = ReviewDatabase.getInstance(this)
    }
}