package com.example.reviewercompose.presentation.screens.review.view.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reviewercompose.presentation.screens.review.creator.ui.ParagraphElementImmutable
import com.example.reviewercompose.presentation.screens.review.creator.ui.ProductRow
import com.example.reviewercompose.presentation.screens.review.view.ReviewViewModel
import com.example.reviewercompose.presentation.theme.ParagraphBackground

@Composable
fun ReviewScreen(
    reviewId: String,
    modifier: Modifier = Modifier,
    viewModel: ReviewViewModel = viewModel(factory = ReviewViewModel.Factory)
) {
    var review by remember { mutableStateOf<ReviewViewModel.ReviewUI?>(null) }
    LaunchedEffect(key1 = reviewId) { review = viewModel.get(reviewId) }

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        review?.let { reviewUI ->
            item {
                ProductRow(product = reviewUI.product)
            }
            items(items = reviewUI.paragraphs) { paragraph ->
                ParagraphElementImmutable(
                    item = paragraph,
                    modifier = modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(4.dp))
                        .background(ParagraphBackground)
                )
            }
        } ?: item {
            Box(Modifier.fillParentMaxWidth()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}