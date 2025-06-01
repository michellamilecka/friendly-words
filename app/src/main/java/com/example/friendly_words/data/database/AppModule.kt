package com.example.friendly_words.data.database

import android.content.Context
import androidx.room.Room
import com.example.friendly_words.data.daos.ImageDao
import com.example.friendly_words.data.daos.ResourceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "friendly_words"
        )
            //.fallbackToDestructiveMigration(true)
            .build()

    @Provides
    fun provideResourceDao(db: AppDatabase): ResourceDao = db.resourceDao()

    @Provides
    fun provideImageDao(db: AppDatabase): ImageDao = db.imageDao()

//    @Provides
//    fun provideResourceImageDao(db: AppDatabase): ResourceImageDao = db.resourceImageDao()

    //todo dodac reszte klas
}
