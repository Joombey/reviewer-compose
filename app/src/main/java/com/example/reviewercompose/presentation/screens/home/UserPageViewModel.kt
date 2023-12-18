package com.example.reviewercompose.presentation.screens.home

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.reviewercompose.ReviewerApplication
import com.example.reviewercompose.data.entities.Review
import com.example.reviewercompose.data.repository.db.DataBaseRepository
import com.example.reviewercompose.data.repository.storage.StorageRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class UserPageViewModel(
    userId: String,
    private val dbRepository: DataBaseRepository,
    private val storageRepository: StorageRepository,
) : ViewModel() {
    suspend fun exit() {
        dbRepository.exit()
    }

    private val _uiState = UserPageUiMutable("", null, emptyList())
    val uiState get() = _uiState as UserPageUiState

    init {
        dbRepository.currentUser
            .onEach { user ->
                _uiState.name = user?.name ?: ""
                _uiState.icon = user?.let {
                    if (it.iconUri == "null") return@let null

                    val uri = Uri.parse(it.iconUri)
                    storageRepository.getBitmapFromUri(uri)
                }
            }
            .launchIn(viewModelScope)
        dbRepository.getUserReviewFor(userId)
            .onEach { reviewList ->
                _uiState.reviews.apply {
                    clear()
                    addAll(reviewList)
                }
            }
            .launchIn(viewModelScope)
    }

    class Factory(private val userId: String) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val app =
                extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ReviewerApplication
            return UserPageViewModel(
                userId,
                app.userRepository,
                app.storageRepository,
            ) as T
        }
    }

    @Stable
    interface UserPageUiState {
        val name: String
        val icon: Bitmap?
        val reviews: List<Review>
    }


    @Stable
    class UserPageUiMutable(
        name: String,
        icon: Bitmap?,
        reviews: List<Review>
    ) : UserPageUiState {
        override var name: String by mutableStateOf(name)
        override var icon: Bitmap? by mutableStateOf(icon)
        override val reviews: MutableList<Review> = reviews.toMutableStateList()

        override fun equals(other: Any?): Boolean {
            return (other as? UserPageUiMutable)?.let {
                name == it.name && icon == it.icon && reviews == it.reviews
            } ?: false
        }
    }
}