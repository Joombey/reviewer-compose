package com.example.reviewercompose.presentation.screens.review.creator.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.reviewercompose.data.entities.Paragraph
import com.example.reviewercompose.presentation.screens.register.ui.ImageWithAddPlaceholder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
    Column(modifier = modifier) {
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
            hint = "Заголовок",
            text = item.title,
            onTextChange = onTitleTextChange,
            textStyle = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = modifier,
            readOnly = readOnly,
            singleLine = true
        )

        EditableText(
            hint = "Текст",
            text = item.text,
            onTextChange = onBodyTextChange,
            textStyle = MaterialTheme.typography.bodyMedium,
            modifier = modifier.height(300.dp),
            readOnly = readOnly,
        )

        HorizontalPager(modifier = modifier.fillMaxWidth(), state = pagerState) { position ->
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                ImageWithAddPlaceholder(
                    modifier = Modifier.align(Alignment.Center),
                    uri = item.photosUris.getOrNull(position)?.let { Uri.parse(it) },
                    onAddButtonPress = { registry.launch(arrayOf("image/*")) }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ParagraphElementImmutable(
    item: Paragraph,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState { item.photosUris.size }
    Column(modifier = modifier) {
        Text(
            text = item.title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        Text(
            text = item.text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            state = pagerState
        ) { position ->
            Box(modifier.fillMaxWidth()){
                ReviewPagerElement(
                    uriString = item.photosUris[position],
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(8.dp),
                )
            }
        }
    }
}

@Composable
fun ReviewPagerElement(
    uriString: String,
    modifier: Modifier = Modifier
) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val contentResolver = LocalContext.current.contentResolver
    LaunchedEffect(key1 = uriString) {
        if (uriString == "null") {
            bitmap = null; return@LaunchedEffect
        }

        val source: ImageDecoder.Source = withContext(Dispatchers.IO) {
            ImageDecoder.createSource(contentResolver, Uri.parse(uriString))
        }
        bitmap = ImageDecoder.decodeBitmap(source)
    }
    Box(modifier) {
        if (bitmap == null) {
            Image(
                contentDescription = null,
                contentScale = ContentScale.Crop,
                imageVector = Icons.Filled.AddBox,
                modifier = Modifier.size(100.dp)
            )
        } else {
            Image(
                contentDescription = null,
                contentScale = ContentScale.Crop,
                bitmap = bitmap!!.asImageBitmap(),
                modifier = Modifier.size(100.dp)
            )
        }
    }
}