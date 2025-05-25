package com.example.friendly_words.data.repositories

import com.example.friendly_words.data.daos.ResourceImageDao
import com.example.friendly_words.data.entities.ResourceImage

class ResourceImageRepository(private val resourceImageDao: ResourceImageDao) {
    suspend fun link(resourceId: Long, imageId: Long) =
        resourceImageDao.insert(ResourceImage(resourceId, imageId))

    suspend fun unlink(resourceId: Long, imageId: Long) =
        resourceImageDao.delete(resourceId, imageId)
}