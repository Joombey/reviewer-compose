package com.example.reviewercompose.presentation.screens.home

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.reviewercompose.ReviewerApplication
import com.example.reviewercompose.data.entities.Review
import com.example.reviewercompose.data.repository.db.DataBaseRepository
import com.example.reviewercompose.data.repository.storage.StorageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class UserPageViewModel(
    userId: String,
    dbRepository: DataBaseRepository,
    storageRepository: StorageRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _uiState = UserPageUiMutable("", null, emptyList())
    val uiState get() = _uiState as UserPageUiState
    init {
        dbRepository.currentUser
            .onEach { user ->
                _uiState.name = user?.name ?: ""
                _uiState.icon = user?.let { storageRepository.getBitmapFromUri(Uri.parse(user.iconUri)) }
            }
            .launchIn(viewModelScope)
        dbRepository.getUserReviewFor(userId)
            .onEach { reviewList->
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
            val savedStateHandle = extras.createSavedStateHandle()
            return UserPageViewModel(
                userId,
                app.userRepository,
                app.storageRepository,
                savedStateHandle
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
    private class UserPageUiMutable(
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