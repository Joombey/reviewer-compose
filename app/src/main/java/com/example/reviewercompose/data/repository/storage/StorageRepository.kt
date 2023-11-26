package com.example.reviewercompose.data.repository.storage

interface StorageRepository {
    suspend fun saveFile(data: ByteArray)
}