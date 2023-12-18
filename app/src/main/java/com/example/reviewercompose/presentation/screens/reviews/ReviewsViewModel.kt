package com.example.reviewercompose.presentation.screens.reviews

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.reviewercompose.ReviewerApplication
import com.example.reviewercompose.data.entities.Product
import com.example.reviewercompose.data.entities.User
import com.example.reviewercompose.data.repository.db.DataBaseRepository
import com.example.reviewercompose.data.repository.storage.StorageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

class ReviewsViewModel(
    private val dbRepo: DataBaseRepository,
    private val storageRepository: StorageRepository
): ViewModel() {
    private val _uiStates = emptyList<ReviewListUI>().toMutableStateList()
    val uiStates: List<ReviewListUI> get() = _uiStates
    init {
        dbRepo.getAllReviews().conflate().onEach { reviews ->
            _uiStates.clear()
            for (review in reviews) {
                _uiStates += ReviewListUI(
                    id = review.id,
                    userIcon = withContext(Dispatchers.IO) { getBitmapFromUser(review.author) } ,
                    userName = review.author.name,
                    title = review.title,
                    product = review.item,
                    date = review.date,
                    paragraphTitles = review.paragraphs.map { it.title }
                )
            }
        }.launchIn(viewModelScope)
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
            return ReviewsViewModel(app.userRepository, app.storageRepository) as T
        }
    }

    data class ReviewListUI(
        val id: String,
        val userIcon: Bitmap?,
        val userName: String,
        val title: String,
        val product: Product,
        val date: String,
        val paragraphTitles: List<String>,
    )
}