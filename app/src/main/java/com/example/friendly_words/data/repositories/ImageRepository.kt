package com.example.friendly_words.data.repositories

import com.example.friendly_words.data.daos.ImageDao
import com.example.friendly_words.data.entities.Image

class ImageRepository(private val imageDao: ImageDao) {
    suspend fun insert(image: Image) = imageDao.insert(image)
    suspend fun delete(image: Image) = imageDao.deleteIfUnreferenced(image)
    suspend fun getAll() = imageDao.getAll()
}