package com.example.friendly_words.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.friendly_words.data.entities.ResourceImage

@Dao
interface ResourceImageDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(resourceImage: ResourceImage)

    @Query("DELETE FROM resource_images WHERE resourceId = :resourceId AND imageId = :imageId")
    suspend fun delete(resourceId: Long, imageId: Long)

}
