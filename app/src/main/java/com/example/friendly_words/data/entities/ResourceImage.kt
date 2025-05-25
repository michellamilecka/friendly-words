package com.example.friendly_words.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "resource_images",
    primaryKeys = ["resourceId", "imageId"],
    foreignKeys = [
        ForeignKey(entity = Resource::class, parentColumns = ["id"], childColumns = ["resourceId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Image::class, parentColumns = ["id"], childColumns = ["imageId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("imageId")]
)
data class ResourceImage(
    val resourceId: Long,
    val imageId: Long
)
