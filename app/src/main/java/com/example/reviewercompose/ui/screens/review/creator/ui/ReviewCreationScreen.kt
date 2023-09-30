package com.example.reviewercompose.ui.screens.review.creator.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reviewercompose.ui.screens.review.creator.ReviewCreationViewModel

@Composable
fun ReviewCreationScreen(
    modifier: Modifier = Modifier,
    reviewViewModel: ReviewCreationViewModel = viewModel()
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "CreateReview",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}