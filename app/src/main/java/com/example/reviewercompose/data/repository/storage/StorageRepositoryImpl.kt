package com.example.reviewercompose.data.repository.storage

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.reviewercompose.ServiceLocator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class StorageRepositoryImpl: StorageRepository {
    private val dir = ServiceLocator.mediaDir
    private val dirCreated get() = File(dir).exists()
    override suspend fun saveFile(data: ByteArray): String = withContext(Dispatchers.IO) {
        val file = File(dir + UUID.randomUUID().toString())
        if (!dirCreated) File(dir).mkdirs()
        file.outputStream().use { it.write(data) }
        file.absolutePath
    }

    override suspend fun saveImage(bitmap: Bitmap): String = withContext(Dispatchers.IO) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val file = File(dir + "${UUID.randomUUID()}.webp")
            if (!dirCreated) File(dir).mkdirs()
            file.outputStream().use {
                bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSLESS, 100, it)
            }
        } else {
            val file = File(dir + "${UUID.randomUUID()}.jpeg")
            if (!dirCreated) File(dir).mkdirs()
            file.outputStream().use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
        }
        ""
    }
}