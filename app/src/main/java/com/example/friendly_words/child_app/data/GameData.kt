package com.example.friendly_words.child_app.data

import com.example.friendly_words.R

data class GameItem(
    val label: String,
    val imageRes: Int,
)

val gameItems = listOf(
    GameItem("Misiu", R.drawable.misiu_1),
    GameItem("Kredka", R.drawable.kredka_1),
    GameItem("But", R.drawable.but_1)
)
