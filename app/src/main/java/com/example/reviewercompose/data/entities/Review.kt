package com.example.reviewercompose.data.entities

data class Review(
    val id: String,
    val author: User,
    val item: ReviewItem,
    val paragraphs: List<Paragraph>
)