package com.example.reviewercompose.data.repository.db

import com.example.reviewercompose.data.db.ReviewDatabase
import com.example.reviewercompose.data.db.tables.ImageEntity
import com.example.reviewercompose.data.db.tables.ItemEntity
import com.example.reviewercompose.data.db.tables.ParagraphEntity
import com.example.reviewercompose.data.db.tables.ReviewEntity
import com.example.reviewercompose.data.db.tables.UserEntity
import com.example.reviewercompose.data.entities.Paragraph
import com.example.reviewercompose.data.entities.Product
import com.example.reviewercompose.data.entities.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import java.lang.System.currentTimeMillis

class DataBaseRepositoryImpl(private val db: ReviewDatabase): DataBaseRepository {
    override suspend fun createUser(user: User, login: String, password: String) {
        val userEntity = UserEntity(
            isCurrent = true,
            name = user.name,
            creationTime = currentTimeMillis(),
            lastOnline = currentTimeMillis(),
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

    override suspend fun createReview(
        user: User,
        product: Product,
        paragraphs: List<Paragraph>,
        imagePath: String?
    ) {
        val item = ItemEntity(model = product.title, image = imagePath)
        db.itemDao.insert(item)
        val review = ReviewEntity(
            title = product.title,
            userId = user.id,
            itemId = item.id,
            creationDate = currentTimeMillis()
        )
        db.reviewDao.insert(review)
        val paragraphs = List(paragraphs.size) { id ->
            val paragraph =ParagraphEntity(
                title = paragraphs[id].title,
                text = paragraphs[id].text,
                reviewId = review.id
            )
            db.paragraphDao.insert(paragraph)
        }
    }

    override val currentUser: Flow<User?> = db.userDao.currentUser()
}