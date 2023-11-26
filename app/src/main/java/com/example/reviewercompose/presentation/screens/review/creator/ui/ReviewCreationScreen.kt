package com.example.reviewercompose.presentation.screens.review.creator.ui


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reviewercompose.R
import com.example.reviewercompose.data.entities.Paragraph
import com.example.reviewercompose.presentation.screens.auth.ui.ReviewerButton
import com.example.reviewercompose.presentation.screens.review.creator.ReviewCreationViewModel
import com.example.reviewercompose.utils.toast
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import java.util.UUID

@Composable
fun ReviewCreationScreen(
    modifier: Modifier = Modifier,
    viewModel: ReviewCreationViewModel = viewModel(factory = ReviewCreationViewModel.Factory)
) {
    Surface(modifier = modifier) {
        val context = LocalContext.current
        val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
        val lazyListState = rememberLazyListState()
        val paragraphs: MutableList<Pair<String, Paragraph>> = rememberSaveable {
            mutableStateListOf(Pair(UUID.randomUUID().toString(), Paragraph()))
        }
        LaunchedEffect(key1 = viewModel) {
            viewModel.errorChannel.receiveAsFlow().collect {
                context.toast(it)
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
            state = lazyListState
        ) {
            item {
                ReviewCreationHeader(
                    onSearchButtonClick = viewModel::query,
                    onProductChoose = viewModel::chooseProduct,
                    uiState = uiState
                )
            }
            itemsIndexed(items = paragraphs, key = { _, item -> item.first }) { id, pair ->
                val item: Paragraph = pair.second
                ParagraphElement(
                    item = item,
                    onTitleTextChange = {
                        paragraphs[id] = paragraphs[id].copy(second = item.copy(title = it))
                    },
                    onBodyTextChange = {
                        paragraphs[id] = paragraphs[id].copy(second = item.copy(text = it))
                    },
                    onImagesChosen = {
                        paragraphs[id].second.photosUris.let { uris ->
                            paragraphs[id] = paragraphs[id].copy(
                                second = item.copy(photosUris = uris - it.toSet() + it)
                            )
                        }
                    }
                )
                ReviewerButton(
                    text = "Добавить абзац",
                    onClick = { paragraphs.add(UUID.randomUUID().toString() to Paragraph()) }
                )
            }
            item {
                ReviewerButton(
                    text = stringResource(R.string.review_creation_screen_label),
                    onClick = { viewModel.create(paragraphs.map { it.second }) }
                )
            }
        }
    }
}