package com.example.shared.data.repositories

import com.example.shared.data.another.ResourceWithImages
import com.example.shared.data.daos.ConfigurationDao
import com.example.shared.data.daos.ResourceDao
import com.example.shared.data.entities.Configuration
import com.example.shared.data.entities.ConfigurationImageUsage
import com.example.shared.data.entities.ConfigurationResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton
import com.example.shared.data.another.ConfigurationMaterialState
import com.example.shared.data.another.VocabularyItem
import com.example.shared.data.another.ConfigurationReinforcementState
import com.example.shared.data.daos.ConfigurationResourceDao

@Singleton
class ConfigurationRepository @Inject constructor(
    private val dao: ConfigurationDao,
    private val resourceDao: ResourceDao,
    private val configurationResourceDao: ConfigurationResourceDao
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

    suspend fun getResourcesWithImagesForActiveConfig(): List<ResourceWithImages> {
        val activeConfig = dao.getActiveConfiguration().firstOrNull() ?: return emptyList()

        // Pobierz wszystkie powiązania ConfigurationResource
        val configResources = dao.getConfigurationResources(activeConfig.id)
        val resourceIds = configResources.map { it.resourceId }

        // Pobierz wszystkie zasoby z obrazami i filtruj po resourceIds
        val allResourcesWithImages = resourceDao.getResourcesWithImages()
        return allResourcesWithImages.filter { it.resource.id in resourceIds }
    }

    suspend fun getMaterialState(configId: Long): ConfigurationMaterialState {
        val resources = getResourcesWithImagesForActiveConfig()
        return ConfigurationMaterialState(
            vocabItems = resources.map { res ->
                VocabularyItem(
                    id = res.resource.id,
                    word = res.resource.name,
                    learnedWord = res.resource.learnedWord,
                    selectedImages = List(res.images.size) { true },
                    inLearningStates = List(res.images.size) { true },
                    inTestStates = List(res.images.size) { false },
                    imagePaths = res.images.map { it.path }
                )
            }
        )
    }

    suspend fun getReinforcementState(configId: Long): ConfigurationReinforcementState {
        // Tymczasowo np. stałe wartości:
        return ConfigurationReinforcementState(
            praiseStates = mapOf(
                "dobrze" to true,
                "super" to true,
                "świetnie" to true,
                "ekstra" to true,
                "rewelacja" to true,
                "brawo" to true
            ),
            animationsEnabled = true
        )
    }

    suspend fun hasMaterialsForActiveConfig(isTestMode: Boolean): Boolean {
        return getResourcesWithImagesForActiveConfigFiltered(isTestMode).isNotEmpty()
    }
    suspend fun getConfigurationNamesUsingResource(resourceId: Long): List<String> {
        val links = configurationResourceDao.getByResourceId(resourceId)
        if (links.isEmpty()) return emptyList()

        val configIds = links.map { it.configurationId }
        val configs = dao.getByIds(configIds)   // patrz niżej
        return configs.map { it.name }
    }

    suspend fun getResourcesWithImagesForActiveConfigFiltered(isTestMode: Boolean): List<ResourceWithImages> {
        val activeConfig = dao.getActiveConfiguration().firstOrNull() ?: return emptyList()

        // zasoby przypięte do tej konfiguracji
        val configResources = dao.getConfigurationResources(activeConfig.id)
        val resourceIds = configResources.map { it.resourceId }.toSet()

        // usage obrazów dla tej konfiguracji
        val usages = dao.getConfigurationImageUsages(activeConfig.id)
        val allowedImageIds = usages
            .filter { usage -> if (isTestMode) usage.inTest else usage.inLearning }
            .map { it.imageId }
            .toSet()

        // pełna lista zasobów z obrazami
        val all = resourceDao.getResourcesWithImages()

        // filtr: tylko zasoby z konfiguracji + odfiltrowane obrazy wg trybu
        return all
            .filter { it.resource.id in resourceIds }
            .map { rwi -> rwi.copy(images = rwi.images.filter { it.id in allowedImageIds }) }
            .filter { it.images.isNotEmpty() }
    }

}
