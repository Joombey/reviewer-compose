package com.example.reviewercompose.data.repository.db

import com.example.reviewercompose.data.db.ReviewDatabase
import com.example.reviewercompose.data.db.tables.ImageEntity
import com.example.reviewercompose.data.db.tables.ItemEntity
import com.example.reviewercompose.data.db.tables.UserEntity
import com.example.reviewercompose.data.entities.Paragraph
import com.example.reviewercompose.data.entities.Product
import com.example.reviewercompose.data.entities.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class DataBaseRepositoryImpl(private val db: ReviewDatabase): DataBaseRepository {
    override suspend fun createUser(user: User, login: String, password: String) {
        val userEntity = UserEntity(
            isCurrent = true,
            name = user.name,
            creationTime = System.currentTimeMillis(),
            lastOnline = System.currentTimeMillis(),
            login = login,
            password = password
        )
        val imageEntity = ImageEntity(
            uri = user.iconUri,
            userId = userEntity.id,
            itemId = null,
            paragraphId = null
        )
        db.userDao.insertUser(userEntity)
        db.imageDao.insertImage(imageEntity)
    }

    override suspend fun createReview(product: Product, paragraphs: List<Paragraph>) {
        val newItem = ItemEntity(
            model = product.title,

        )
        db.itemDao.insert()
    }

    private val ioScope = CoroutineScope(Dispatchers.IO)
    override val currentUser: Flow<User?> = db.userDao.currentUser()
}