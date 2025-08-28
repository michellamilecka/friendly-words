package com.example.shared.data.repositories

import com.example.shared.data.daos.ImageDao
import com.example.shared.data.entities.Image
import javax.inject.Inject

class ImageRepository @Inject constructor(private val imageDao: ImageDao) {
    suspend fun insert(image: Image) = imageDao.insert(image)
    suspend fun delete(image: Image) = imageDao.delete(image)
    suspend fun getAll() = imageDao.getAll()
    suspend fun getByResourceId(resourceId: Long): List<Image> = imageDao.getByResourceId(resourceId)
    suspend fun deleteUnassignedImages() {
        imageDao.getAll().forEach {
            if (it.resourceId == null) {
                imageDao.delete(it)
            }
        }
    }


}