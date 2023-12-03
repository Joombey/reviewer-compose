package com.example.reviewercompose.data.repository.db

import com.example.reviewercompose.data.db.ReviewDatabase
import com.example.reviewercompose.data.db.tables.ImageEntity
import com.example.reviewercompose.data.db.tables.ItemEntity
import com.example.reviewercompose.data.db.tables.ParagraphEntity
import com.example.reviewercompose.data.db.tables.ReviewEntity
import com.example.reviewercompose.data.db.tables.UserEntity
import com.example.reviewercompose.data.entities.Paragraph
import com.example.reviewercompose.data.entities.Product
import com.example.reviewercompose.data.entities.Review
import com.example.reviewercompose.data.entities.User
import com.example.reviewercompose.data.repository.storage.StorageRepository
import com.example.reviewercompose.presentation.screens.review.creator.ui.ProductRow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import java.lang.System.currentTimeMillis
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DataBaseRepositoryImpl(
    private val db: ReviewDatabase,
    private val storageRepository: StorageRepository
) : DataBaseRepository {
    override suspend fun createUser(user: User, login: String, password: String) {
        val userEntity = UserEntity(
            isCurrent = true,
            name = user.name,
            creationTime = currentTimeMillis(),
            lastOnline = currentTimeMillis(),
            login = login,
            password = password
        )
        val imageEntity = ImageEntity(uri = user.iconUri, userId = userEntity.id)
        db.userDao.insertUser(userEntity)
        db.imageDao.insertImage(imageEntity)
    }

    override suspend fun createReview(
        user: User,
        product: Product,
        title: String,
        paragraphs: List<Paragraph>,
        imagePath: String?
    ) {
        val item = ItemEntity(model = product.title, image = imagePath)
        db.itemDao.insert(item)
        val review = ReviewEntity(
            title = title,
            userId = user.id,
            itemId = item.id
        )
        db.reviewDao.insert(review)
        for (paragraph in paragraphs) {
            val paragraphEntity = ParagraphEntity(
                title = paragraph.title,
                text = paragraph.text,
                reviewId = review.id
            )
            db.paragraphDao.insert(paragraphEntity)
            paragraph.photosUris.forEach {
                val image = ImageEntity(uri = it, paragraphId = paragraphEntity.id)
                db.imageDao.insertImage(image)
            }
        }
    }

    override fun getUserReviewFor(userId: String): Flow<List<Review>> {
        return db.reviewDao.getUserReviewList(userId).map { list ->
            withContext(Dispatchers.IO) {
                list.map { reviewEntity ->
                    val item = db.itemDao.getById(reviewEntity.itemId).let { itemEntity ->
                        Product(
                            productId = itemEntity.id,
                            title = itemEntity.model,
                            image = itemEntity.image?.let { storageRepository.getBitmapFromPath(it) },
                            rating = itemEntity.rating?.toString() ?: "N/A"
                        )
                    }
                    val user = db.userDao.getCurrentUser()
                    val paragraphs = db.paragraphDao.getByReviewId(reviewEntity.id).map {
                        val uris = db.imageDao.getByParagraphId(it.id)
                        Paragraph(
                            title = it.title ?: item.title,
                            text = it.text,
                            photosUris = uris
                        )
                    }
                    Review(
                        id = reviewEntity.id,
                        author = user,
                        item = item,
                        title = reviewEntity.title,
                        paragraphs = paragraphs,
                        date = reviewEntity.creationDate.asDateTimeString()
                    )
                }
            }
        }
    }

    override val currentUser: Flow<User?> = db.userDao.currentUser()
}

fun Long.asDateTimeString(): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
    return sdf.format(Date(this))
}