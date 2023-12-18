package com.example.reviewercompose.presentation.screens.review.view

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.reviewercompose.ReviewerApplication
import com.example.reviewercompose.data.entities.Paragraph
import com.example.reviewercompose.data.entities.Product
import com.example.reviewercompose.data.entities.Review
import com.example.reviewercompose.data.entities.User
import com.example.reviewercompose.data.repository.db.DataBaseRepository
import com.example.reviewercompose.data.repository.storage.StorageRepository
import com.example.reviewercompose.presentation.screens.review.creator.ReviewCreationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReviewViewModel(
    private val dbRepo: DataBaseRepository,
    private val storageRepository: StorageRepository
): ViewModel() {
    suspend fun get(reviewID: String): ReviewUI = dbRepo.getReviewByID(reviewID).toReviewUI()

    data class ReviewUI(
        val title: String,
        val userName: String,
        val userIcon: Bitmap?,
        val product: Product,
        val paragraphs: List<Paragraph>
    )

    private suspend fun Review.toReviewUI(): ReviewUI {
        return ReviewUI(
            title = title,
            userName = author.name,
            userIcon = withContext(Dispatchers.IO) { getBitmapFromUser(author) },
            product = item,
            paragraphs = paragraphs
        )
    }

    private suspend fun getBitmapFromUser(user: User): Bitmap? {
        if (user.iconUri == "null") return null
        val uri = Uri.parse(user.iconUri)
        return storageRepository.getBitmapFromUri(uri)
    }

    companion object Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val app =
                extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ReviewerApplication
            return ReviewViewModel(
                app.userRepository,
                app.storageRepository,
            ) as T
        }
    }
}