package com.example.reviewercompose

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.reviewercompose.data.repository.DataBaseRepository
import com.example.reviewercompose.data.entities.User
import com.example.reviewercompose.presentation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    private val userRepository: DataBaseRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _currentUser = MutableStateFlow<UserAuthState>(UserAuthState.Unauthorized)
    val availableScreens: StateFlow<List<Screen>> = flow {
        _currentUser.collect {
            when (it) {
                is UserAuthState.Authorized -> emit(Screen.authenticatedScreens)
                UserAuthState.Unauthorized -> emit(Screen.unAuthenticatedScreens)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = Screen.unAuthenticatedScreens
    )

    init {
        userRepository.currentUser
            .onEach {
                if (it == null) _currentUser.value = UserAuthState.Unauthorized
                else _currentUser.value = UserAuthState.Authorized(it)
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    val allScreens: List<Screen> = listOf(
        Screen.ReviewListScreen,
        Screen.ReviewCreationScreen,
        Screen.UserProfileScreen,
        Screen.AuthGraph
    )

    companion object Factory: ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val app = extras[APPLICATION_KEY] as ReviewerApplication
            val savedStateHandle = extras.createSavedStateHandle()
            return MainViewModel(app.userRepository, savedStateHandle) as T
        }
    }
}

sealed interface UserAuthState {
    object Unauthorized : UserAuthState
    data class Authorized(val user: User) : UserAuthState
}