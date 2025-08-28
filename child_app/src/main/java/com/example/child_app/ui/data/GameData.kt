package com.example.child_app.ui.data

import com.example.child_app.R

data class GameItem(
    val label: String,
    val imageRes: Int,
)

val gameItems = listOf(
    GameItem("Misiu", R.drawable.misiu_1),
    GameItem("Kredka", R.drawable.kredka_1),
    GameItem("But", R.drawable.but_1)
)
