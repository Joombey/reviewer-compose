package com.example.reviewercompose.data.entities


data class Paragraph(
    val title: String = "",
    val text: String = "",
    val photosUris: List<String> = emptyList(),
)
