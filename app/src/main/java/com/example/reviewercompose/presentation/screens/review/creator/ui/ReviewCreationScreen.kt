package com.example.reviewercompose.presentation.screens.review.creator.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reviewercompose.R
import com.example.reviewercompose.data.entities.Paragraph
import com.example.reviewercompose.data.entities.User
import com.example.reviewercompose.presentation.screens.auth.ui.ReviewerButton
import com.example.reviewercompose.presentation.screens.auth.ui.ReviewerTextField
import com.example.reviewercompose.presentation.screens.review.creator.ReviewCreationEvent
import com.example.reviewercompose.presentation.screens.review.creator.ReviewCreationViewModel
import com.example.reviewercompose.presentation.theme.ParagraphBackground
import com.example.reviewercompose.utils.toast
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun ReviewCreationScreen(
    onReviewCreated: () -> Unit,
    user: User,
    modifier: Modifier = Modifier,
    viewModel: ReviewCreationViewModel = viewModel(factory = ReviewCreationViewModel.Factory)
) {
    Surface(modifier = modifier.padding(horizontal = 8.dp)) {
        val context = LocalContext.current
        val uiState by viewModel.headerUiState.collectAsStateWithLifecycle()
        val lazyListState = rememberLazyListState()
        val paragraphs: MutableList<Pair<String, Paragraph>> = rememberSavableParagraphList(
            listOf(UUID.randomUUID().toString() to Paragraph()).toMutableStateList()
        )
        val latestReviewCreation by rememberUpdatedState(onReviewCreated)
        LaunchedEffect(key1 = viewModel) {
            launch {
                viewModel.errorChannel.receiveAsFlow().collect { context.toast(it) }
            }
            launch {
                viewModel.eventChannel.receiveAsFlow().collect {
                    if (it == ReviewCreationEvent.ReviewCreated) { latestReviewCreation() }
                }
            }
        }
        var title by rememberSaveable { mutableStateOf("") }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = lazyListState,
        ) {
            item(contentType = "Header") {
                Column(Modifier.padding(vertical = 8.dp)) {
                    ReviewerTextField(
                        hint = stringResource(R.string.review_title_label),
                        text = title,
                        onTextChange = { title = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    ReviewCreationHeader(
                        onSearchButtonClick = viewModel::query,
                        onProductChoose = viewModel::chooseProduct,
                        uiState = uiState,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
            itemsIndexed(
                items = paragraphs,
                key = { _, item -> item.first },
                contentType = { _, _ -> "list" })
            { id, pair ->
                val item: Paragraph = pair.second
                Column(modifier = Modifier.fillMaxWidth()) {
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
                        },
                        modifier = modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp))
                            .background(ParagraphBackground),
                        readOnly = false
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    ReviewerButton(
                        modifier = Modifier.clip(
                            RoundedCornerShape(
                                bottomStart = 4.dp,
                                bottomEnd = 4.dp
                            )
                        ),
                        text = "Добавить абзац",
                        onClick = { paragraphs.add(UUID.randomUUID().toString() to Paragraph()) }
                    )
                }
            }
            item {
                ReviewerButton(
                    text = stringResource(R.string.review_creation_screen_label),
                    onClick = {
                        viewModel.create(user, title, paragraphs.map { it.second })
                    },
                    modifier = Modifier
                )
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
@Composable
fun rememberSavableParagraphList(initialList: List<Pair<String, Paragraph>> = emptyList()): MutableList<Pair<String, Paragraph>> {
    val saver: Saver<SnapshotStateList<Pair<String, Paragraph>>, *> = listSaver(
        save = { listToSave ->
            listToSave.map {
                listOf(it.first, it.second.title, it.second.text, it.second.photosUris)
            }
        },
        restore = { listToRestore ->
            listToRestore.toMutableList().map {
                Pair(
                    it[0] as String,
                    Paragraph(it[1] as String, it[2] as String, it[3] as List<String>)
                )
            }.toMutableStateList()
        }
    )
    return rememberSaveable(saver = saver) { initialList.toMutableStateList() }
}