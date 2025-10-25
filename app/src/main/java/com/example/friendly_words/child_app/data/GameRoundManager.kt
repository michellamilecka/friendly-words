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
    settings: RoundSettings,
    isTestMode: Boolean
): List<GameRound> {
    val activeConfig = repository.getActiveConfiguration().firstOrNull() ?: return emptyList()

    val allResources = repository.getResourcesWithImagesForActiveConfigFiltered(isTestMode)
    if (allResources.isEmpty()) return emptyList()

    val numberOfWords = settings.numberOfWords.coerceAtMost(allResources.size)
    val selectedResources = allResources.shuffled().take(numberOfWords)

    val rounds = mutableListOf<GameRound>()

    selectedResources.forEach { res ->
        val allowedImagesForCorrect = res.images
        if (allowedImagesForCorrect.isEmpty()) return@forEach

        repeat(settings.repetitionPerWord.coerceAtLeast(1)) {
            val correctImage = allowedImagesForCorrect.random()
            val correctItem = GameItem(
                label = res.resource.learnedWord,
                imagePath = correctImage.path
            )

            val distractorResources = (allResources - res)
                .filter { it.images.isNotEmpty() }
                .shuffled()
                .take((settings.displayedImagesCount - 1).coerceAtLeast(0))

            val distractors = distractorResources.map { dr ->
                val img = dr.images.random()
                GameItem(
                    label = dr.resource.learnedWord,
                    imagePath = img.path
                )
            }

            val options = (distractors + correctItem).shuffled()
            rounds.add(GameRound(correctItem, options))
        }
    }

    return rounds.shuffled()
}