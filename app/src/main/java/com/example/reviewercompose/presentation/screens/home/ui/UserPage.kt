package com.example.reviewercompose.presentation.screens.home.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reviewercompose.presentation.screens.auth.ui.ReviewerButton
import com.example.reviewercompose.presentation.screens.home.UserPageViewModel
import com.example.reviewercompose.presentation.screens.reviews.ui.ReviewElement

@Composable
fun UserPageScreen(
    userId: String,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val viewModel: UserPageViewModel = viewModel(factory = UserPageViewModel.Factory(userId = userId))
    val uiState = viewModel.uiState
    LazyColumn (
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = listState
    ){
        item {
            UserPageHeader(uiState.name, uiState.icon, Modifier.fillParentMaxWidth())
            Divider(Modifier.padding(top = 4.dp).fillMaxWidth(), 2.dp, Color.Black)
        }
        items(items = uiState.reviews) {
            ReviewElement(
                userIcon = uiState.icon!!,
                itemImageBitmap = it.item.image!!,
                title = it.title,
                paragraphs = it.paragraphs.map { p -> p.title },
                date = it.date,
                userName = it.author.name,
                modifier = Modifier.fillParentMaxWidth()
            )
        }
    }
}

@Composable
fun UserPageHeader(name: String, icon: Bitmap?, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val circleModifier = Modifier
            .size(150.dp)
            .clip(CircleShape)
        if (icon == null) {
            Spacer(
                modifier = circleModifier
                    .background(Color.Gray)
            )
        } else {
            Image(
                bitmap = icon.asImageBitmap(),
                contentDescription = null,
                modifier = circleModifier,
                contentScale = ContentScale.Crop
            )
        }
        Text(text = name, style = MaterialTheme.typography.labelLarge.copy(fontSize = 24.sp))
        ReviewerButton(text = "Выйти", onClick = {  })
    }
}