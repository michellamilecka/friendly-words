package com.example.friendly_words.data.repositories

import com.example.friendly_words.data.daos.ImageDao
import com.example.friendly_words.data.daos.ResourceDao
import com.example.friendly_words.data.database.AppDatabase
import com.example.friendly_words.data.entities.Resource
import javax.inject.Inject


class ResourceRepository @Inject constructor(
    private val resourceDao: ResourceDao
) {
    suspend fun insert(resource: Resource) = resourceDao.insert(resource)
    suspend fun getAll() = resourceDao.getAll()
    suspend fun delete(resource: Resource) = resourceDao.delete(resource)

}