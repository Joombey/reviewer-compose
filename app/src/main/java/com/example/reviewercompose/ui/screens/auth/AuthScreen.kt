package com.example.reviewercompose.ui.screens.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    userPageViewModel: AuthViewModel = viewModel()
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Auth",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}