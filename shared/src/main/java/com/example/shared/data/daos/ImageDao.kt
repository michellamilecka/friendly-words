package com.example.shared.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.shared.data.entities.Image
import com.example.shared.data.entities.ResourceImage

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(image: Image): Long

    @Insert
    suspend fun insertMany(images: List<Image>): List<Long>

    @Query("SELECT * FROM images")
    suspend fun getAll(): List<Image>

    @Query("SELECT COUNT(*) FROM resource_images WHERE imageId = :imageId")
    suspend fun getReferenceCount(imageId: Long): Int

//    @Transaction
//    suspend fun deleteIfUnreferenced(image: Image) {
//        if (getReferenceCount(image.id) == 0) {
//            delete(image)
//        } else {
//            throw IllegalStateException("Cannot delete image: it is still referenced by a resource.")
//        }
//    }
    @Query("SELECT images.* FROM images" +
            " INNER JOIN resource_images ON images.id = resource_images.imageId" +
            " WHERE resource_images.resourceId = :resourceId")
    suspend fun getByResourceId(resourceId: Long): List<Image>

    @Delete
    suspend fun delete(image: Image)

    // crudy do obslugi encji laczacej zasoby z obrazami
    @Query("DELETE FROM resource_images WHERE resourceId = :resourceId")
    suspend fun deleteImageLinksForResource(resourceId: Long)

    @Insert
    suspend fun insertResourceImageLinks(links: List<ResourceImage>)

    @Query("DELETE FROM resource_images WHERE resourceId = :resourceId AND imageId IN (:imageIds)")
    suspend fun deleteSpecificImageLinks(resourceId: Long, imageIds: List<Long>)

}
