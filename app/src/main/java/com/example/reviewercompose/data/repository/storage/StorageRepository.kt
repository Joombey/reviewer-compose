package com.example.reviewercompose.data.repository.storage

import android.graphics.Bitmap
import android.net.Uri

interface StorageRepository {
    suspend fun saveFile(data: ByteArray): String

    suspend fun saveImage(bitmap: Bitmap): String

    suspend fun getBitmapFromPath(path: String): Bitmap?

    suspend fun getBitmapFromUri(uri: Uri): Bitmap?
}