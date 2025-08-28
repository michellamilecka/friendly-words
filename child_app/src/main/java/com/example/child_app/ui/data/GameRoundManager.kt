package com.example.child_app.ui.data


data class GameRound(
    val correctItem: GameItem,
    val options: List<GameItem>
)

fun generateGameRounds(): List<GameRound> {
    val selectedWords = gameItems.shuffled().take(GameSettings.numberOfWordsToTeach)
    val rounds = mutableListOf<GameRound>()

    selectedWords.forEach { word ->
        repeat(GameSettings.repetitionsPerWord) {
            val distractors = gameItems.filter { it != word }.shuffled().take(2)
            val options = (distractors + word).shuffled()
            rounds.add(GameRound(correctItem = word, options = options))
        }
    }

    return rounds.shuffled()
}
