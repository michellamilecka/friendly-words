package com.example.friendly_words.data.repositories

import com.example.friendly_words.data.daos.ConfigurationDao
import com.example.friendly_words.data.entities.Configuration
import com.example.friendly_words.data.entities.ConfigurationImageUsage
import com.example.friendly_words.data.entities.ConfigurationResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigurationRepository @Inject constructor(
    private val dao: ConfigurationDao
) {

    // metody dla konfiguracji
    fun getAll(): Flow<List<Configuration>> = dao.getAll()
    suspend fun getAllOnce(): List<Configuration> = dao.getAllOnce()

    suspend fun getById(id: Long): Configuration = dao.getById(id)

    suspend fun insert(configuration: Configuration): Long = dao.insert(configuration)

    suspend fun delete(configuration: Configuration) = dao.delete(configuration)

    suspend fun update(configuration: Configuration) = dao.update(configuration)

    suspend fun setActiveConfiguration(configuration: Configuration, mode: String) {
        dao.clearActiveConfiguration()
        dao.activateConfiguration(configuration.id, mode)
    }

    fun getActiveConfiguration(): Flow<Configuration?> = dao.getActiveConfiguration()

    // metody dla encji laczacej konfiguracje z zasobami/materialami
    suspend fun addResource(link: ConfigurationResource) = dao.insertConfigurationResource(link)

    suspend fun insertResources(resources: List<ConfigurationResource>) { dao.insertConfigurationResources(resources) }

    suspend fun removeSingleResource(configurationId: Long, resourceId: Long) = dao.deleteSingleConfigurationResource(configurationId, resourceId)

    suspend fun getResources(configurationId: Long): List<ConfigurationResource> = dao.getConfigurationResources(configurationId)

    suspend fun deleteResourcesByConfigId(configId: Long) { dao.deleteConfigurationResourcesByConfigId(configId) }

    // metody dla encji laczacej konfiguracje z obrazami dla zasobow znajdujacych sie w konfiguracji
    suspend fun addImageUsage(usage: ConfigurationImageUsage) = dao.insertConfigurationImageUsage(usage)

    suspend fun insertImageUsages(usages: List<ConfigurationImageUsage>) { dao.insertConfigurationImageUsages(usages) }

    suspend fun removeAllImageUsages(configurationId: Long) = dao.deleteConfigurationImageUsagesForConfiguration(configurationId)

    suspend fun removeSingleImageUsage(configurationId: Long, imageId: Long) = dao.deleteSingleConfigurationImageUsage(configurationId, imageId)

    suspend fun getImageUsages(configurationId: Long): List<ConfigurationImageUsage> = dao.getConfigurationImageUsages(configurationId)

    suspend fun deleteImageUsagesByConfigId(configId: Long) { dao.deleteConfigurationImageUsagesByConfigId(configId) }
}
