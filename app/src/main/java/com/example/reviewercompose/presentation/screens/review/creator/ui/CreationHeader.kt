package com.example.reviewercompose.presentation.screens.review.creator.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.reviewercompose.data.entities.Product
import com.example.reviewercompose.presentation.screens.auth.ui.ReviewerTextField
import com.example.reviewercompose.presentation.screens.review.creator.ReviewCreationHeaderUiState

@Composable
fun ReviewCreationHeader(
    onSearchButtonClick: (String) -> Unit,
    onProductChoose: (Product) -> Unit,
    uiState: ReviewCreationHeaderUiState,
    modifier: Modifier = Modifier,
) {
    var query: String by rememberSaveable { mutableStateOf("") }
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        if (uiState is ReviewCreationHeaderUiState.Searching) {
            ProductSearchBar(
                contentList = uiState.productList,
                queryText = query,
                onQueryChange = { query = it },
                onSearchButtonClick = onSearchButtonClick,
                onProductChoose = onProductChoose
            )
        } else {
            ProductTitleBar()
        }
    }
}

@Composable
fun ProductSearchBar(
    contentList: List<Product>,
    queryText: String,
    onQueryChange: (String) -> Unit,
    onSearchButtonClick: (String) -> Unit,
    onProductChoose: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 4.dp),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 100.dp),
    ) {
        item {
            ReviewerTextField(
                hint = "Товар",
                text = queryText,
                onTextChange = onQueryChange,
                keyboardType = KeyboardType.Text,
                trailingIcon = {
                    Icon(
                        modifier = Modifier.clickable { onSearchButtonClick(queryText) },
                        imageVector = Icons.Filled.Search,
                        contentDescription = null
                    )
                }
            )
        }
        items(items = contentList, key = { it.hashCode() }) {
            ProductRow(
                item = it,
                modifier = Modifier.clickable { onProductChoose(it) }
            )
        }
    }
}

@Composable
fun ProductTitleBar() {

}

@Composable
fun ProductRow(
    item: Product,
    bitmap: Bitmap? = null,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier,
    ) {
        if (bitmap == null) {
            Spacer(
                Modifier
                    .size(width = 60.dp, height = 50.dp)
                    .background(Color.Gray)
            )
        } else {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .size(width = 60.dp, height = 50.dp)
                    .weight(1f)
            )
        }
        Text(
            text = item.title,
            modifier = Modifier.weight(1f)
        )
    }
}