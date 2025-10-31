package com.example.shared.data.daos

import androidx.room.*
import com.example.shared.data.entities.Configuration
import com.example.shared.data.entities.ConfigurationImageUsage
import com.example.shared.data.entities.ConfigurationResource
import kotlinx.coroutines.flow.Flow

@Dao
interface ConfigurationDao {

    // crudy dla konfiguracji
    @Query("SELECT * FROM configurations")
    fun getAll(): Flow<List<Configuration>>

    @Query("SELECT * FROM configurations WHERE id = :id")
    suspend fun getById(id: Long): Configuration

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(configuration: Configuration): Long
    @Query("SELECT * FROM configurations")
    suspend fun getAllOnce(): List<Configuration>
    @Delete
    suspend fun delete(configuration: Configuration)

    @Query("SELECT * FROM configurations WHERE id IN (:ids)")
    suspend fun getByIds(ids: List<Long>): List<Configuration>

    @Update
    suspend fun update(configuration: Configuration)

    @Query("UPDATE configurations SET isActive = 0, activeMode = NULL WHERE isActive = 1")
    suspend fun clearActiveConfiguration()

    @Query("UPDATE configurations SET isActive = 1, activeMode = :mode WHERE id = :id")
    suspend fun activateConfiguration(id: Long, mode: String)

    @Query("SELECT * FROM configurations WHERE isActive = 1")
    fun getActiveConfiguration(): Flow<Configuration?>



    // crudy dla encji laczacej konfiguracje z zasobem/materialem
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfigurationResource(link: ConfigurationResource)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfigurationResources(resources: List<ConfigurationResource>)

    @Query("SELECT * FROM configuration_resources WHERE configurationId = :configurationId")
    suspend fun getConfigurationResources(configurationId: Long): List<ConfigurationResource>

    @Query("DELETE FROM configuration_resources WHERE configurationId = :configurationId AND resourceId = :resourceId")
    suspend fun deleteSingleConfigurationResource(configurationId: Long, resourceId: Long)

    @Query("DELETE FROM configuration_resources WHERE configurationId = :configId")
    suspend fun deleteConfigurationResourcesByConfigId(configId: Long)



    //crudy dla encji laczacej konfiguracje z obrazami dla zasobow znajdujacych sie w konfiguracji
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfigurationImageUsage(usage: ConfigurationImageUsage)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfigurationImageUsages(usages: List<ConfigurationImageUsage>)

    @Query("SELECT * FROM configuration_image_usages WHERE configurationId = :configurationId")
    suspend fun getConfigurationImageUsages(configurationId: Long): List<ConfigurationImageUsage>

    @Query("DELETE FROM configuration_image_usages WHERE configurationId = :configurationId")
    suspend fun deleteConfigurationImageUsagesForConfiguration(configurationId: Long)

    @Query("DELETE FROM configuration_image_usages WHERE configurationId = :configurationId AND imageId = :imageId")
    suspend fun deleteSingleConfigurationImageUsage(configurationId: Long, imageId: Long)

    @Query("DELETE FROM configuration_image_usages WHERE configurationId = :configId")
    suspend fun deleteConfigurationImageUsagesByConfigId(configId: Long)
}
