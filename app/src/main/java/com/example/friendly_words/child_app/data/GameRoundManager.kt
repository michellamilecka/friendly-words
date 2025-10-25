package com.example.friendly_words.child_app.data

import com.example.shared.data.another.RoundSettings
import com.example.shared.data.repositories.ConfigurationRepository
import kotlinx.coroutines.flow.firstOrNull

data class GameRound(
    val correctItem: GameItem,
    val options: List<GameItem>
)

suspend fun generateGameRounds(
    repository: ConfigurationRepository,
    settings: RoundSettings
): List<GameRound> {
    val activeConfig = repository.getActiveConfiguration().firstOrNull() ?: return emptyList()

    val allResources = repository.getResourcesWithImagesForActiveConfig()
    if (allResources.isEmpty()) return emptyList()

    val selectedResources = allResources.shuffled().take(settings.numberOfWords)
    val rounds = mutableListOf<GameRound>()

    selectedResources.forEach { resource ->
        repeat(settings.repetitionPerWord) {
            val correctItem = GameItem(
                label = resource.resource.learnedWord,
                imagePath = resource.images.firstOrNull()?.path ?: ""
            )

            val distractors = allResources
                .filter { it != resource && it.images.isNotEmpty() }
                .shuffled()
                .take((settings.displayedImagesCount - 1).coerceAtLeast(0))
                .map { GameItem(it.resource.learnedWord, it.images.first().path) }

            val options = (distractors + correctItem).shuffled()
            rounds.add(GameRound(correctItem, options))
        }
    }

    return rounds.shuffled()
}