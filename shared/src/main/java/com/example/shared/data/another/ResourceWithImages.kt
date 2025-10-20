package com.example.shared.data.another

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.shared.data.entities.Image
import com.example.shared.data.entities.Resource
import com.example.shared.data.entities.ResourceImage

data class ResourceWithImages(
    @Embedded val resource: Resource,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(ResourceImage::class, parentColumn = "resourceId", entityColumn = "imageId")
    )
    val images: List<Image>
)
