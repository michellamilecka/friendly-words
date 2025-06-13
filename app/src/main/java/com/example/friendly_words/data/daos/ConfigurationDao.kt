package com.example.friendly_words.data.daos

import androidx.room.*
import com.example.friendly_words.data.entities.Configuration
import kotlinx.coroutines.flow.Flow

@Dao
interface ConfigurationDao {

    @Query("SELECT * FROM configurations")
    fun getAll(): Flow<List<Configuration>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(configuration: Configuration): Long

    @Delete
    suspend fun delete(configuration: Configuration)

    @Update
    suspend fun update(configuration: Configuration)
}
