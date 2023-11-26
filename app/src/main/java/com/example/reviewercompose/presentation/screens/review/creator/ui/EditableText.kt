package com.example.reviewercompose.presentation.screens.review.creator.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditableText(
    text: String,
    onTextChange: (String) -> Unit,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    readOnly: Boolean
) {
    var focused by remember { mutableStateOf(false) }
    val focusRequester by rememberSaveable { mutableStateOf(FocusRequester()) }
    Box(
        modifier
            .focusRequester(focusRequester)
            .onFocusChanged { focused = it.hasFocus }
    ) {
        if (focused && !readOnly) {
            TextField(value = text, onValueChange = onTextChange, textStyle = textStyle)
        } else {
            Text(text = text, style = textStyle)
        }
    }
}