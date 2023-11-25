package com.example.reviewercompose.presentation.screens.review.creator

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.reviewercompose.ReviewerApplication
import com.example.reviewercompose.data.api.QueryResult
import com.example.reviewercompose.data.api.SerpApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReviewCreationViewModel(
    private val apiRepository: SerpApiRepository ,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    fun makeTest(query: String) = viewModelScope.launch(Dispatchers.IO) {
        val result = apiRepository.getList(query)
        when(result) {
            is QueryResult.SerpApiShoppingResponse -> Log.i("viewModel", result.shoppingResults.toString())
            QueryResult.ClientError -> Log.i("viewModel", "4xx")
            QueryResult.ServerError -> Log.i("viewModel", "5xx")
        }
    }

    companion object Factory: ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val app = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ReviewerApplication
            val savedStateHandle = extras.createSavedStateHandle()
            return ReviewCreationViewModel(app.apiRepository, savedStateHandle) as T
        }
    }
}