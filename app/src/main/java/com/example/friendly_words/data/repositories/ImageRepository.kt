package com.example.friendly_words.data.repositories

import com.example.friendly_words.data.daos.ImageDao
import com.example.friendly_words.data.entities.Image
import javax.inject.Inject

class ImageRepository @Inject constructor(private val imageDao: ImageDao) {
    suspend fun insert(image: Image) = imageDao.insert(image)
    suspend fun delete(image: Image) = imageDao.delete(image)
    suspend fun getAll() = imageDao.getAll()
}