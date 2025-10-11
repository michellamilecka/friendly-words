package com.example.shared.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "resources")
data class Resource(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val learnedWord: String,
    val category: String = "",
    val isExample:Boolean = false,
)