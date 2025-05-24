package com.example.friendly_words.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "configuration_resources",
    primaryKeys = ["configurationId", "resourceId"],
    foreignKeys = [
        ForeignKey(entity = Configuration::class, parentColumns = ["id"], childColumns = ["configurationId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Resource::class, parentColumns = ["id"], childColumns = ["resourceId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("resourceId")]
)
data class ConfigurationResource(
    val configurationId: Long,
    val resourceId: Long
)