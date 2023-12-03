package com.example.reviewercompose.data.entities

data class Review(
    val id: String,
    val title: String,
    val date: String,
    val author: User,
    val item: Product,
    val paragraphs: List<Paragraph>
)