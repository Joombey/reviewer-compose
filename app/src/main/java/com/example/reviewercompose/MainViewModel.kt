package com.example.reviewercompose

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.reviewercompose.data.repository.db.DataBaseRepository
import com.example.reviewercompose.data.entities.User
import com.example.reviewercompose.presentation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.plus

class MainViewModel(
    private val userRepository: DataBaseRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _userAuthState = MutableStateFlow<UserAuthState>(UserAuthState.Unauthorized)
    val userAuthState get() = _userAuthState.asStateFlow()

    init {
        userRepository.currentUser
            .onEach {
                if (it == null) _userAuthState.value = UserAuthState.Unauthorized
                else _userAuthState.value = UserAuthState.Authorized(it)
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    companion object Factory: ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val app = extras[APPLICATION_KEY] as ReviewerApplication
            val savedStateHandle = extras.createSavedStateHandle()
            return MainViewModel(app.userRepository, savedStateHandle) as T
        }
    }
}

sealed class UserAuthState(val availableScreen: List<Screen>) {
    object Unauthorized : UserAuthState(Screen.unAuthenticatedScreens)
    data class Authorized(val user: User) : UserAuthState(Screen.authenticatedScreens)
}