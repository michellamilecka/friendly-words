package com.example.friendly_words.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.friendly_words.data.daos.ImageDao
import com.example.friendly_words.data.daos.ResourceDao
import com.example.friendly_words.data.entities.Image
import com.example.friendly_words.data.entities.Resource


@Database(
    entities = [
        Resource::class,
        Image::class
        //ResourceImage::class,
        // todo dodanie reszty encji jak juz dao beda zrobione
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase(){
    abstract fun resourceDao(): ResourceDao
    abstract fun imageDao() : ImageDao
    //abstract fun resourceImageDao() : ResourceImageDao
}