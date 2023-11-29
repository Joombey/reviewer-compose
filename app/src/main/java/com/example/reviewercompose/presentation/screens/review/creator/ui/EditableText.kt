package com.example.reviewercompose.presentation.screens.review.creator.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import com.example.reviewercompose.presentation.screens.auth.ui.ReviewerTextField

@Composable
fun EditableText(
    hint: String,
    text: String,
    onTextChange: (String) -> Unit,
    textStyle: TextStyle,
    readOnly: Boolean,
    modifier: Modifier = Modifier,
    singleLine: Boolean = false
) {
    Box(modifier) {
        if (!readOnly) {
            ReviewerTextField(
                modifier = modifier,
                hint = hint,
                text = text, onTextChange = onTextChange,
                keyboardType = KeyboardType.Text,
                textStyle = textStyle,
                singleLine = singleLine
            )
        } else {
            Text(modifier = modifier, text = hint, style = textStyle)
        }
    }
}