package com.example.friendly_words.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.friendly_words.data.another.Converters
import com.example.friendly_words.data.daos.ConfigurationDao
import com.example.friendly_words.data.daos.ImageDao
import com.example.friendly_words.data.daos.ResourceDao
import com.example.friendly_words.data.entities.Image
import com.example.friendly_words.data.entities.Resource
import com.example.friendly_words.data.entities.Configuration
import com.example.friendly_words.data.entities.ConfigurationImageUsage
import com.example.friendly_words.data.entities.ConfigurationResource


@Database(
    entities = [
        Resource::class,
        Image::class,
        Configuration::class,
        ConfigurationResource::class,
        ConfigurationImageUsage::class
        // todo dodanie reszty encji jak juz dao beda zrobione
    ],
    version = 12,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(){
    abstract fun resourceDao(): ResourceDao
    abstract fun imageDao() : ImageDao
    abstract fun configurationDao(): ConfigurationDao
    //abstract fun resourceImageDao() : ResourceImageDao
}