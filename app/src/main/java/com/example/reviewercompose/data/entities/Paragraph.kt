package com.example.reviewercompose.data.entities

import kotlinx.serialization.Serializable

@Serializable
data class Paragraph(
    val title: String = "",
    val text: String = "",
    val photosUris: List<String> = emptyList(),
)
