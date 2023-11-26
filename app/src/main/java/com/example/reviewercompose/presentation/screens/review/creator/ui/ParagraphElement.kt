package com.example.reviewercompose.presentation.screens.review.creator.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.reviewercompose.data.entities.Paragraph
import com.example.reviewercompose.presentation.screens.register.ui.ImageWithAddPlaceholder
import com.example.reviewercompose.presentation.theme.ParagraphBackground

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ParagraphElement(
    item: Paragraph,
    onTitleTextChange: (String) -> Unit,
    onBodyTextChange: (String) -> Unit,
    onImagesChosen: (List<String>) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
) {
    Column(
        modifier = modifier
            .background(ParagraphBackground)
            .clip(RoundedCornerShape(4.dp))
    ) {
        val context = LocalContext.current
        val pagerState = rememberPagerState { item.photosUris.size + 1 }
        val registry = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenMultipleDocuments(),
            onResult = { uris: List<@JvmSuppressWildcards Uri> ->
                uris.forEach { uri ->
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }
                onImagesChosen(uris.map { it.toString() })
            }
        )
        EditableText(
            text = item.title,
            onTextChange = onTitleTextChange,
            textStyle = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.fillMaxWidth(),
            readOnly = readOnly
        )

        EditableText(
            text = item.text,
            onTextChange = onBodyTextChange,
            textStyle = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth(),
            readOnly = readOnly
        )

        HorizontalPager(pagerState) { position ->
            ImageWithAddPlaceholder(
                uri = item.photosUris.getOrNull(position)?.let { Uri.parse(it) },
                onAddButtonPress = { registry.launch(arrayOf("image/*")) }
            )
        }
    }
}