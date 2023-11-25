package com.example.reviewercompose.presentation.screens.review.creator.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reviewercompose.presentation.screens.auth.ui.ReviewerButton
import com.example.reviewercompose.presentation.screens.auth.ui.ReviewerText
import com.example.reviewercompose.presentation.screens.review.creator.ReviewCreationViewModel

@Composable
fun ReviewCreationScreen(
    modifier: Modifier = Modifier,
    viewModel: ReviewCreationViewModel = viewModel(factory = ReviewCreationViewModel.Factory)
) {
    Surface(
        modifier = modifier,
    ) {
        var text by rememberSaveable { mutableStateOf("") }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ReviewerText(
                hint = "Query",
                text = text,
                onTextChange = { text = it },
                keyboardType = KeyboardType.Text
            )

            ReviewerButton(
                text = "Press Me",
                onClick = { viewModel.makeTest(text) },
                modifier = Modifier
                    .width(70.dp)
            )
        }
    }
}