package com.example.reviewercompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reviewercompose.data.entities.User
import com.example.reviewercompose.presentation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class MainViewModel : ViewModel() {
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

    val allScreens: List<Screen> = listOf(
        Screen.ReviewListScreen,
        Screen.ReviewCreationScreen,
        Screen.UserProfileScreen,
        Screen.AuthGraph
    )
}

sealed interface UserAuthState {
    object Unauthorized : UserAuthState
    data class Authorized(val user: User) : UserAuthState
}