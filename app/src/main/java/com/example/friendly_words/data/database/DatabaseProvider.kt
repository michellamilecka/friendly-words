package com.example.friendly_words.data.database

import android.content.Context
import androidx.room.Room

class DatabaseProvider {
    @Volatile // gdy jeden wątek zaktualizuje INSTANCE, drugi wątek zobaczy już nową wartość i nie stworzy drugiego obiektu
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "friendly_words"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}