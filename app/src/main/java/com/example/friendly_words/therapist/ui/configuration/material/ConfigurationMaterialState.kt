package com.example.friendly_words.therapist.ui.configuration.material

import com.example.friendly_words.data.entities.ConfigurationImageUsage
import com.example.friendly_words.data.entities.Resource
import com.example.friendly_words.data.repositories.ImageRepository

data class ConfigurationMaterialState(
    val vocabItems: List<VocabularyItem> = emptyList(),
    val availableWordsToAdd: List<Resource> = emptyList(),
    val selectedWordIndex: Int = 0,
    val wordIndexToDelete: Int? = null,
    val showDeleteDialog: Boolean = false,
    val showAddDialog: Boolean = false
)

suspend fun ConfigurationMaterialState.toConfigurationImageUsages(
    configurationId: Long,
    imageRepository: ImageRepository
): List<ConfigurationImageUsage> {
    return vocabItems.flatMap { item ->
        val images = imageRepository.getByResourceId(item.id)
        images.mapIndexedNotNull { index, image ->
            if (item.selectedImages.getOrNull(index) == true) {
                ConfigurationImageUsage(
                    configurationId = configurationId,
                    imageId = image.id,
                    inLearning = item.inLearningStates.getOrNull(index) == true,
                    inTest = item.inTestStates.getOrNull(index) == true
                )
            } else null
        }
    }
}
