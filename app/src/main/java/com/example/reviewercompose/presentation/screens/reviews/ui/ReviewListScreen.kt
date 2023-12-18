package com.example.reviewercompose.presentation.screens.reviews.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reviewercompose.presentation.screens.reviews.ReviewsViewModel

@Composable
fun ReviewListScreen(
    onReviewClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReviewsViewModel = viewModel(factory = ReviewsViewModel.Factory)
) {
    val listState = rememberLazyListState()
    val reviews = viewModel.uiStates
    LazyColumn(
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = listState
    ) {
        items(items = reviews) {
            ReviewElement(
                userIcon = it.userIcon,
                itemImageBitmap = it.product.image,
                title = it.title,
                paragraphs = it.paragraphTitles,
                date = it.date,
                userName = it.userName,
                modifier = Modifier
                    .fillParentMaxWidth()
                    .clickable { onReviewClick(it.id) }
            )
        }
    }
}