package com.example.friendly_words.child_app.data

import com.example.shared.data.entities.toLearningSettings
import com.example.shared.data.repositories.ConfigurationRepository
import kotlinx.coroutines.flow.firstOrNull

data class GameRound(
    val correctItem: GameItem,
    val options: List<GameItem>
)

suspend fun generateGameRounds(repository: ConfigurationRepository): List<GameRound> {
    val activeConfig = repository.getActiveConfiguration().firstOrNull() ?: return emptyList()
    val materialState = repository.getMaterialState(activeConfig.id)
    val reinforcementState = repository.getReinforcementState(activeConfig.id)
    val settings = activeConfig.toLearningSettings(materialState, reinforcementState)

    // Pobierz zasoby z obrazami
    val allResources = repository.getResourcesWithImagesForActiveConfig()
    println("generateGameRounds → resources: ${allResources.size}")
    if (allResources.isEmpty()) return emptyList()

    // Wybierz losowo tyle słów ile potrzebujemy
    val selectedResources = allResources.shuffled().take(settings.numberOfWords)
    val rounds = mutableListOf<GameRound>()

    selectedResources.forEach { resource ->
        repeat(settings.repetitionPerWord) {
            // Poprawny item
            val correctItem = GameItem(
                label = resource.resource.learnedWord,
                imagePath = resource.images.firstOrNull()?.path ?: ""
            )

            // Distraktory
            val distractors = allResources
                .filter { it != resource && it.images.isNotEmpty() }
                .shuffled()
                .take(settings.displayedImagesCount - 1)
                .map { GameItem(it.resource.learnedWord, it.images.first().path) }

            // Opcje rundy: correctItem + distractors
            val options = (distractors + correctItem).shuffled()

            // Dodaj rundę
            rounds.add(GameRound(correctItem = correctItem, options = options))
        }
    }

    println("generateGameRounds → rounds.size: ${rounds.size}")
    return rounds.shuffled()
}
