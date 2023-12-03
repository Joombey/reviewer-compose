package com.example.reviewercompose.presentation.screens.review.creator

import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.util.Log
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
import com.example.reviewercompose.data.entities.User
import com.example.reviewercompose.data.repository.db.DataBaseRepository
import com.example.reviewercompose.data.repository.storage.StorageRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ReviewCreationViewModel(
    private val apiRepository: SerpApiRepository,
    private val dbRepository: DataBaseRepository,
    private val storageRepository: StorageRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _eventChannel = Channel<ReviewCreationEvent>()
    val eventChannel: ReceiveChannel<ReviewCreationEvent> get() = _eventChannel
    //message event channel
    private val _errorChannel = Channel<String>()

    val errorChannel: ReceiveChannel<String> = _errorChannel
    //uiState
    private val _headerUiState =
        MutableStateFlow<ReviewCreationHeaderUiState>(ReviewCreationHeaderUiState.NotChosen)

    val headerUiState: StateFlow<ReviewCreationHeaderUiState> get() = _headerUiState

    //image caching process job to stop when needed
    private var cachingJob: Job? = null

    fun create(user: User, title: String, paragraphs: List<Paragraph>) = viewModelScope.launch(Dispatchers.IO) {
        (headerUiState.value as? ReviewCreationHeaderUiState.ProductChosen)?.let { state ->
            val path = state.chosenProduct.image?.let { bitmap ->
                storageRepository.saveImage(bitmap)
            }
            dbRepository.createReview(user, state.chosenProduct, title, paragraphs, path)
            _eventChannel.send(ReviewCreationEvent.ReviewCreated)
        } ?: _errorChannel.send("Предмет обзора не выбран")
    }

    fun query(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            cachingJob?.cancelAndJoin()
            _headerUiState.update { ReviewCreationHeaderUiState.Searching(emptyList()) }
            when (val result = apiRepository.query(query)) {
                QueryResult.ClientError -> _headerUiState.update {
                    ReviewCreationHeaderUiState.Searching(
                        isLoading = false,
                        productList = emptyList(),
                        errorCode = 400
                    )
                }

                is QueryResult.SerpApiShoppingResponse -> {
                    _headerUiState.update {
                        ReviewCreationHeaderUiState.Searching(
                            isLoading = false,
                            productList = result.shoppingResults.map {
                                Product(
                                    productId = it.productId,
                                    title = it.title,
                                    rating = it.rating ?: "N/A"
                                )
                            }
                        )
                    }
                    val urlList = result.shoppingResults.map { it.imageUrl }
                    delay(100L)
                    cachingJob = beginCaching(urlList)
                    cachingJob?.invokeOnCompletion { cachingJob = null }
                }

                QueryResult.ServerError -> _headerUiState.update {
                    ReviewCreationHeaderUiState.Searching(
                        isLoading = false,
                        productList = emptyList(),
                        errorCode = 500
                    )
                }
            }
        }
    }

    fun chooseProduct(product: Product) {
        _headerUiState.update {
            ReviewCreationHeaderUiState.ProductChosen(product)
        }
    }

    private fun beginCaching(productImages: List<String>) =
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            Log.e("ktor", "beginCaching: $throwable", throwable)
        }) {
            productImages.forEachIndexed { index, image ->
                val data = apiRepository.fetchBytes(image)
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val source = ImageDecoder.createSource(data)
                    ImageDecoder.decodeBitmap(source)
                } else {
                    BitmapFactory.decodeByteArray(data, 0, data.size)
                }
                Log.i("bitmap2", "$bitmap")
                if (!isActive) return@forEachIndexed
                _headerUiState.update { state ->
                    if (state !is ReviewCreationHeaderUiState.Searching) {
                        state
                    } else {
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
                app.storageRepository,
                savedStateHandle
            ) as T
        }
    }
}

enum class ReviewCreationEvent {
    ReviewCreated
}

sealed interface ReviewCreationHeaderUiState {
    data class Searching(
        val productList: List<Product>,
        val isLoading: Boolean = true,
        val errorCode: Int? = null
    ) : ReviewCreationHeaderUiState

    data class ProductChosen(
        val chosenProduct: Product
    ) : ReviewCreationHeaderUiState

    object NotChosen : ReviewCreationHeaderUiState
}