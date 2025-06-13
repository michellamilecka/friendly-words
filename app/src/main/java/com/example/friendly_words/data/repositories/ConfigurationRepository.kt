package com.example.friendly_words.data.repositories

import com.example.friendly_words.data.daos.ConfigurationDao
import com.example.friendly_words.data.entities.Configuration
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigurationRepository @Inject constructor(
    private val dao: ConfigurationDao
) {

    fun getAll(): Flow<List<Configuration>> = dao.getAll()

    suspend fun insert(configuration: Configuration): Long = dao.insert(configuration)

    suspend fun delete(configuration: Configuration) = dao.delete(configuration)

    suspend fun update(configuration: Configuration) = dao.update(configuration)
}
