package com.example.reviewercompose.ui.screens.reviews.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reviewercompose.ui.screens.reviews.ReviewsViewModel

@Composable
fun ReviewListScreen(
    modifier: Modifier = Modifier,
    reviewsViewModel: ReviewsViewModel = viewModel()
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "List",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}