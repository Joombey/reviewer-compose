package com.example.reviewercompose.data.repository.storage

import com.example.reviewercompose.ServiceLocator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

class StorageRepositoryImpl: StorageRepository {
    val dir = ServiceLocator.mediaDir
    val dirCreated get() = File(dir).exists()
    override suspend fun saveFile(data: ByteArray): Unit = withContext(Dispatchers.IO) {
        val file = File(dir + UUID.randomUUID().toString())
        if (!dirCreated) File(dir).mkdirs()
        file.outputStream().use { it.write(data) }
    }
}