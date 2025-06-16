package com.example.friendly_words.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.friendly_words.data.daos.ConfigurationDao
import com.example.friendly_words.data.daos.ImageDao
import com.example.friendly_words.data.daos.ResourceDao
import com.example.friendly_words.data.repositories.ConfigurationRepository
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
            .fallbackToDestructiveMigration(true)
            .addCallback(object : RoomDatabase.Callback() {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    db.execSQL("PRAGMA foreign_keys=ON;") // to umozliwia dzialanie foreign keys
                }
            })
            .build()

    @Provides
    fun provideResourceDao(db: AppDatabase): ResourceDao = db.resourceDao()

    @Provides
    fun provideImageDao(db: AppDatabase): ImageDao = db.imageDao()

    @Provides
    fun provideConfigurationDao(db: AppDatabase): ConfigurationDao =
        db.configurationDao()
    @Provides
    fun provideConfigurationRepository(dao: ConfigurationDao): ConfigurationRepository =
        ConfigurationRepository(dao)

//    @Provides
//    fun provideResourceImageDao(db: AppDatabase): ResourceImageDao = db.resourceImageDao()

    //todo dodac reszte klas
}
