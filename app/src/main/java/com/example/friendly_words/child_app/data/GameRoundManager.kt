package com.example.friendly_words.child_app.data

import com.example.shared.data.entities.Resource


data class GameRound(
    val correctItem: GameItem,
    val options: List<GameItem>
)

fun generateGameRounds(resources: List<Resource>): List<GameRound> {
    val selectedResources = resources.shuffled().take(com.example.friendly_words.child_app.data.GameSettings.numberOfWordsToTeach)
    val rounds = mutableListOf<GameRound>()

    selectedResources.forEach { resource ->
        val word = resource.toGameItem()
        repeat(com.example.friendly_words.child_app.data.GameSettings.repetitionsPerWord) {
            // losowe distractory spośród innych zasobów
            val distractors = resources.filter { it != resource }.shuffled().take(2).map { it.toGameItem() }
            val options = (distractors + word).shuffled()
            rounds.add(GameRound(correctItem = word, options = options))
        }
    }

    return rounds.shuffled()
}

