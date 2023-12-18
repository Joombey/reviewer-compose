package com.example.reviewercompose.presentation.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.reviewercompose.ReviewerApplication
import com.example.reviewercompose.data.repository.db.DataBaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthViewModel(private val userRepo: DataBaseRepository): ViewModel() {
    suspend fun tryAuth(login: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            userRepo.getUserIdByLoginPass(login, password) ?: false
            userRepo.switchUser(login, password)
            true
        }
    }

    companion object Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val app = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ReviewerApplication
            return AuthViewModel(app.userRepository) as T
        }
    }
}