package com.example.friendly_words.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index


@Entity(
    tableName = "configuration_resource_images",
    primaryKeys = ["configurationId", "resourceId", "imageId"],
    foreignKeys = [
        ForeignKey(entity = Configuration::class, parentColumns = ["id"], childColumns = ["configurationId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Resource::class, parentColumns = ["id"], childColumns = ["resourceId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Image::class, parentColumns = ["id"], childColumns = ["imageId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("resourceId"), Index("imageId")]
)
data class ConfigurationResourceImage(
    val configurationId: Long,
    val resourceId: Long,
    val imageId: Long,
    val inLearning: Boolean,
    val inTest: Boolean
)