package com.example.shared.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.shared.data.entities.Image

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(image: Image): Long

    @Insert
    suspend fun insertMany(images: List<Image>)

    @Query("SELECT * FROM images")
    suspend fun getAll(): List<Image>

//    @Query("SELECT COUNT(*) FROM resource_images WHERE imageId = :imageId")
//    suspend fun getReferenceCount(imageId: Long): Int

//    @Transaction
//    suspend fun deleteIfUnreferenced(image: Image) {
//        if (getReferenceCount(image.id) == 0) {
//            delete(image)
//        } else {
//            throw IllegalStateException("Cannot delete image: it is still referenced by a resource.")
//        }
//    }
    @Query("SELECT * FROM images WHERE resourceId = :resourceId")
    suspend fun getByResourceId(resourceId: Long): List<Image>

    @Delete
    suspend fun delete(image: Image)
}
