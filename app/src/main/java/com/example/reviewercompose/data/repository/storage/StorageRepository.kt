package com.example.reviewercompose.data.repository.storage

import android.graphics.Bitmap

interface StorageRepository {
    suspend fun saveFile(data: ByteArray): String

    suspend fun saveImage(bitmap: Bitmap): String
}