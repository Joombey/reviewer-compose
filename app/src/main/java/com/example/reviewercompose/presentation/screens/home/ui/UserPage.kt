package com.example.reviewercompose.presentation.screens.home.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reviewercompose.presentation.screens.home.UserPageViewModel

@Composable
fun UserPageScreen(
    modifier: Modifier = Modifier,
    userPageViewModel: UserPageViewModel = viewModel()
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "User",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}