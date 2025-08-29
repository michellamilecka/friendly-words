package com.example.shared.data.another

import com.example.shared.data.entities.ConfigurationImageUsage
import com.example.shared.data.entities.Resource
import com.example.shared.data.repositories.ImageRepository

data class VocabularyItem(
    val id: Long,
    val word: String,
    val learnedWord: String,
    val selectedImages: List<Boolean>,
    val inLearningStates: List<Boolean>,
    val inTestStates: List<Boolean>,
    val imagePaths: List<String>
)

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
