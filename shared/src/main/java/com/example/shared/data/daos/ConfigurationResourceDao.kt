package com.example.shared.data.daos

import androidx.room.Dao
import androidx.room.Query
import com.example.shared.data.entities.ConfigurationResource

@Dao
interface ConfigurationResourceDao {

    @Query("SELECT * FROM configuration_resources WHERE resourceId = :resourceId")
    suspend fun getByResourceId(resourceId: Long): List<ConfigurationResource>
}
