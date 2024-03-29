package com.example.reviewercompose.data.entities

import android.graphics.Bitmap

data class Product(
    val productId: String,
    val title: String,
    val image: Bitmap? = null,
    val rating: String,
)
