package com.example.reviewercompose.presentation.screens.review.creator.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.CircularProgressIndicator
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
    Log.i("state2", uiState.toString())
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        when (uiState) {
            is ReviewCreationHeaderUiState.ProductChosen -> {
                ProductRow(
                    product = uiState.chosenProduct,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            is ReviewCreationHeaderUiState.Searching -> {
                ProductSearchBar(
                    contentList = uiState.productList,
                    queryText = query,
                    onQueryChange = { query = it },
                    onSearchButtonClick = onSearchButtonClick,
                    onProductChoose = onProductChoose,
                    errorCode = uiState.errorCode,
                    isLoading = uiState.isLoading,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            ReviewCreationHeaderUiState.NotChosen -> {
                ReviewerTextField(
                    hint = "Товар",
                    text = query,
                    onTextChange = { query = it },
                    keyboardType = KeyboardType.Text,
                    trailingIcon = {
                        Icon(
                            modifier = Modifier.clickable { onSearchButtonClick(query) },
                            imageVector = Icons.Filled.Search,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
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
    isLoading: Boolean,
    errorCode: Int?,
    modifier: Modifier = Modifier
) {
    Column {
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
            },
            modifier = modifier
        )
        when (errorCode) {
            400 -> {
                Text(text = "Проблема с интернетом")
            }

            500 -> {
                Text(text = "Проблема с cервером")
            }

            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 250.dp),
                ) {
                    when {
                        errorCode == 400 -> {
                            item {
                                Text(text = "Проблема с интернетом")
                            }
                        }

                        errorCode == 500 -> {
                            item {
                                Text(text = "Проблема с cервером")
                            }
                        }

                        isLoading -> {
                            item{
                                Box(Modifier.fillMaxWidth().heightIn(70.dp)) {
                                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                                }
                            }
                        }

                        else -> {
                            items(items = contentList, key = { it.hashCode() }) {
                                ProductRow(
                                    product = it,
                                    modifier = Modifier.clickable { onProductChoose(it) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductRow(
    product: Product,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier,
    ) {
        Column {
            if (product.image == null) {
                Spacer(
                    Modifier
                        .size(width = 60.dp, height = 50.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Gray)
                )
            } else {
                Image(
                    bitmap = product.image.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(width = 60.dp, height = 50.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
            Row {
                Icon(imageVector = Icons.Filled.StarBorder, contentDescription = null)
                Text(
                    modifier = Modifier
                        .paddingFromBaseline(bottom = 4.dp)
                        .padding(4.dp), text = product.rating
                )
            }
        }
        Text(modifier = Modifier.padding(horizontal = 4.dp), text = product.title)
    }
}