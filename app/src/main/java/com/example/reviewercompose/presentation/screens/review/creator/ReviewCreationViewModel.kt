package com.example.reviewercompose.presentation.screens.review.creator

import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.reviewercompose.ReviewerApplication
import com.example.reviewercompose.data.repository.api.QueryResult
import com.example.reviewercompose.data.repository.api.SerpApiRepository
import com.example.reviewercompose.data.entities.Paragraph
import com.example.reviewercompose.data.entities.Product
import com.example.reviewercompose.data.repository.db.DataBaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ReviewCreationViewModel(
    private val apiRepository: SerpApiRepository,
    private val dbRepository: DataBaseRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    //message event channel
    private val _errorChannel = Channel<String>()
    val errorChannel: ReceiveChannel<String> = _errorChannel
    //uiState
    private val _uiStateFlow = MutableStateFlow<ReviewCreationHeaderUiState>(ReviewCreationHeaderUiState.Idle)
    val uiStateFlow: StateFlow<ReviewCreationHeaderUiState> get() = _uiStateFlow

    //image caching process job to stop when needed
    private var cachingJob: Job? = null

    fun create(paragraphs: List<Paragraph>) = viewModelScope.launch(Dispatchers.IO) {
        (uiStateFlow.value as? ReviewCreationHeaderUiState.Creating)?.let {
            dbRepository.createReview(it.chosenProduct, paragraphs)
        }
    }

    fun query(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            cachingJob?.cancelAndJoin()
            when (val result = apiRepository.query(query)) {
                QueryResult.ClientError -> _uiStateFlow.update {
                    ReviewCreationHeaderUiState.ClientErrorWhileSearching(
                        result as QueryResult.ClientError
                    )
                }

                is QueryResult.SerpApiShoppingResponse -> {
                    _uiStateFlow.update {
                        ReviewCreationHeaderUiState.Searching(
                            result.shoppingResults.map {
                                Product(
                                    productId = it.productId,
                                    title = it.title,
                                    rating = it.rating ?: "N/A"
                                )
                            }
                        )
                    }
                    val urlList = result.shoppingResults.map { it.imageUrl }
                    cachingJob = beginCaching(urlList)
                    cachingJob?.invokeOnCompletion { cachingJob = null }
                }

                QueryResult.ServerError -> _uiStateFlow.update {
                    ReviewCreationHeaderUiState.ServerErrorWhileSearching(
                        result as QueryResult.ServerError
                    )
                }
            }
        }
    }

    fun chooseProduct(product: Product) {
        _uiStateFlow.update { ReviewCreationHeaderUiState.Creating(product) }
    }

    private fun beginCaching(productImages: List<String>) = viewModelScope.launch(Dispatchers.IO) {
        productImages.forEachIndexed { index, image ->
            val data = apiRepository.fetchBytes(image)
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val source = ImageDecoder.createSource(data)
                ImageDecoder.decodeBitmap(source)
            } else {
                BitmapFactory.decodeByteArray(data, 0, data.size)
            }

            if (!isActive) return@forEachIndexed
            _uiStateFlow.update { state ->
                if (state !is ReviewCreationHeaderUiState.Searching) state
                else {
                    val listWithImage = state.productList
                        .toMutableList()
                        .also { it[index] = it[index].copy(image = bitmap) }
                    state.copy(productList = listWithImage)
                }
            }
        }
    }

    companion object Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val app =
                extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ReviewerApplication
            val savedStateHandle = extras.createSavedStateHandle()
            return ReviewCreationViewModel(
                app.apiRepository,
                app.userRepository,
                savedStateHandle
            ) as T
        }
    }
}

sealed interface ReviewCreationHeaderUiState {
    data class Searching(
        val productList: List<Product>
    ) : ReviewCreationHeaderUiState

    data class Creating(
        val chosenProduct: Product
    ) : ReviewCreationHeaderUiState

    data class ClientErrorWhileSearching(
        val error: QueryResult.ClientError = QueryResult.ClientError
    ) : ReviewCreationHeaderUiState

    data class ServerErrorWhileSearching(
        val error: QueryResult.ServerError = QueryResult.ServerError
    ) : ReviewCreationHeaderUiState

    object Idle : ReviewCreationHeaderUiState
}