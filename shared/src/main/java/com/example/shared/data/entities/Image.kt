package com.example.shared.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "images",
    foreignKeys = [
        ForeignKey(entity = Resource::class, parentColumns = ["id"], childColumns = ["resourceId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("resourceId")]
)
data class Image(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val path: String,
    val resourceId: Long? = null
)