package com.example.reviewercompose.data.repository.storage

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import com.example.reviewercompose.ServiceLocator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

class StorageRepositoryImpl(private val contentResolver: ContentResolver): StorageRepository {
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
            file.absolutePath
        } else {
            val file = File(dir + "${UUID.randomUUID()}.jpeg")
            if (!dirCreated) File(dir).mkdirs()
            file.outputStream().use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
            file.absolutePath
        }
    }


    override suspend fun getBitmapFromUri(uri: Uri): Bitmap? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val data = contentResolver.openInputStream(uri)?.use {
                it.readBytes()
            }
            val source = ImageDecoder.createSource(data ?: return null)
            ImageDecoder.decodeBitmap(source)
        }
        else {
            contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it)
            }
        }
    }

    override suspend fun getBitmapFromPath(path: String): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            ImageDecoder.createSource(File(path)).let {
                ImageDecoder.decodeBitmap(it)
            }
        }
        else {
            BitmapFactory.decodeFile(path)
        }
    }
}