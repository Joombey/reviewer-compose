package com.example.reviewercompose.presentation.screens.reviews.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ReviewElement(
    userIcon: Bitmap,
    itemImageBitmap: Bitmap,
    title: String,
    date: String,
    userName: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(8.dp),
    ) {
        Box() {
            Image(
                bitmap = itemImageBitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = date,
                modifier = Modifier
                    .padding(8.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .clip(RoundedCornerShape(12.dp))
                    .align(Alignment.BottomEnd),
                style = MaterialTheme.typography.labelMedium.copy(fontStyle = FontStyle.Italic)
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        Row(modifier = Modifier.heightIn(100.dp)) {
            Icon(
                bitmap = userIcon.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = userName,
                style = MaterialTheme.typography.bodyMedium
            )
        }

//        Button(onClick = { /*TODO*/ }) {
//
//        }
    }
}

@Composable
fun UserIconWithTitle(
    userIcon: Bitmap,
    title: String,
    date: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Icon(
            modifier = Modifier
                .heightIn(200.dp)
                .width(120.dp)
                .clip(CircleShape),
            bitmap = userIcon.asImageBitmap(),
            contentDescription = null,
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = date,
                style = MaterialTheme.typography.bodySmall,
                fontStyle = FontStyle.Italic,
                fontSize = 12.sp
            )
        }
    }
}

@Preview()
@Composable
fun ReviewElementPreview() {
    Box(modifier = Modifier.fillMaxSize()) {

    }
}