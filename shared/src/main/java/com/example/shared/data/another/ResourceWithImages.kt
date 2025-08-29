package com.example.shared.data.another

import androidx.room.Embedded
import androidx.room.Relation
import com.example.shared.data.entities.Image
import com.example.shared.data.entities.Resource

data class ResourceWithImages(
    @Embedded val resource: Resource,
    @Relation(
        parentColumn = "id",
        entityColumn = "resourceId"
    )
    val images: List<Image>
)
