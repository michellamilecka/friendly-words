package com.example.friendly_words.data.another

import androidx.room.Embedded
import androidx.room.Relation
import com.example.friendly_words.data.entities.Image
import com.example.friendly_words.data.entities.Resource

data class ResourceWithImages(
    @Embedded val resource: Resource,
    @Relation(
        parentColumn = "id",
        entityColumn = "resourceId"
    )
    val images: List<Image>
)
