package com.example.reviewercompose.presentation.screens.register

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.reviewercompose.ReviewerApplication
import com.example.reviewercompose.data.DataBaseRepository
import com.example.reviewercompose.data.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val userRepository: DataBaseRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    fun register(user: User, login: String, password: String) =
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.createUser(user, login, password)
        }

    companion object Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val app = extras[APPLICATION_KEY] as ReviewerApplication
            val savedStateHandle = extras.createSavedStateHandle()
            return RegistrationViewModel(app.userRepository, savedStateHandle) as T
        }
    }
}