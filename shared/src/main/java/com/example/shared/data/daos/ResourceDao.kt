package com.example.shared.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.shared.data.another.ResourceWithImages
import com.example.shared.data.entities.Resource
import kotlinx.coroutines.flow.Flow

@Dao
interface ResourceDao {

    @Insert
    suspend fun insert(resource: Resource): Long

    @Query("SELECT * FROM resources")
    fun getAll(): Flow<List<Resource>>

    @Query("SELECT * FROM resources")
    suspend fun getAllOnce(): List<Resource>

    @Query("SELECT * FROM resources WHERE id = :resourceId")
    suspend fun getById(resourceId: Long): Resource

    @Query("SELECT * FROM resources WHERE name = :name LIMIT 1")
    suspend fun getByName(name: String): Resource?

    @Delete
    suspend fun delete(resource: Resource)

    @Update
    suspend fun update(resource: Resource)


    @Transaction
    @Query("SELECT * FROM resources")
    suspend fun getResourcesWithImages(): List<ResourceWithImages>

}