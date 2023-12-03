package com.example.reviewercompose.presentation.screens.reviews.ui

import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.PopupProperties

@Composable
fun ReviewElement(
    userIcon: Bitmap,
    itemImageBitmap: Bitmap,
    title: String,
    paragraphs: List<String>,
    date: String,
    userName: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(2.dp, MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(12.dp))
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(8.dp),
    ) {
        ImageWithText(itemImageBitmap, date, Modifier.fillMaxWidth())
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        UserRow(userIcon = userIcon, userName = userName, Modifier.height(40.dp).fillMaxWidth())
        if (paragraphs.size == 1) {
            Text(
                text = paragraphs[0],
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
        } else {
            DropDownParagraphMenu(paragraphs, Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun DropDownParagraphMenu(
    titleList: List<String>,
    modifier: Modifier = Modifier
) {
    var rowSize by remember { mutableStateOf(Size.Zero) }
    var expanded by rememberSaveable { mutableStateOf(false) }
    var chosenIndex by rememberSaveable { mutableIntStateOf(0) }
    Log.i("par", "$titleList")
    Row(
        modifier
            .clickable { expanded = !expanded }
            .onGloballyPositioned { rowSize = it.size.toSize() }
    ) {
        Text(text = titleList[chosenIndex], Modifier.clickable { expanded = !expanded })
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(with(LocalDensity.current) { rowSize.width.toDp() })
        ) {
            titleList.forEachIndexed { index, title ->
                DropdownMenuItem(
                    text = { Text(title) },
                    onClick = { chosenIndex = index; expanded = false },
                    contentPadding = PaddingValues(8.dp),
                    trailingIcon = { Text("${index + 1}") },
                )
            }
        }
    }
}

@Composable
fun UserRow(userIcon: Bitmap, userName: String, modifier: Modifier) {
    var rowSize by remember { mutableStateOf(Size.Zero) }
    Row(
        modifier.onGloballyPositioned { rowSize = it.size.toSize() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            bitmap = userIcon.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
//                .weight(1f)
                .size(with(LocalDensity.current) { rowSize.height.toDp() })
                .clip(CircleShape)
        )
        Text(
            text = userName,
            modifier = Modifier.weight(3f),
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ImageWithText(image: Bitmap, date: String, modifier: Modifier = Modifier) {
    Box(modifier) {
        Image(
            bitmap = image.asImageBitmap(),
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
}

@Preview()
@Composable
fun ReviewElementPreview() {
    Box(modifier = Modifier.fillMaxSize()) {

    }
}