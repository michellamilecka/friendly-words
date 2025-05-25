package com.example.friendly_words.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.friendly_words.data.entities.Image
import com.example.friendly_words.data.entities.Resource
import com.example.friendly_words.data.entities.ResourceImage

@Dao
interface ResourceDao {

    @Insert
    suspend fun insert(resource: Resource): Long

    @Query("SELECT * FROM resources")
    suspend fun getAll(): List<Resource>

    @Query("SELECT * FROM resources WHERE id = :resourceId")
    suspend fun getById(resourceId: Long): Resource

    @Delete
    suspend fun delete(resource: Resource)

    //wtf is even this
    @Query("""
    SELECT images.* FROM images
    INNER JOIN resource_images ON images.id = resource_images.imageId
    WHERE resource_images.resourceId = :resourceId
""")
    suspend fun getImagesForResource(resourceId: Long): List<Image>
}