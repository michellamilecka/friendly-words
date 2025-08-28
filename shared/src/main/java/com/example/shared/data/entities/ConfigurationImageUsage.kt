package com.example.shared.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index


@Entity(
    tableName = "configuration_image_usages",
    primaryKeys = ["configurationId", "imageId"],
    foreignKeys = [
        ForeignKey(entity = Configuration::class, parentColumns = ["id"], childColumns = ["configurationId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Image::class, parentColumns = ["id"], childColumns = ["imageId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("imageId")]
)
data class ConfigurationImageUsage(
    val configurationId: Long,
    val imageId: Long,
    val inLearning: Boolean,
    val inTest: Boolean
)