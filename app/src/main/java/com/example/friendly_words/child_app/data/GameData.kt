package com.example.friendly_words.child_app.data

import com.example.friendly_words.R
import com.example.shared.data.entities.Resource

data class GameItem(
    val label: String,
    val imageRes: Int, // tutaj możesz mapować np. pierwsze przypisane Image.id albo ścieżkę
)

fun Resource.toGameItem(): GameItem {
    // TODO: dopasuj Image z Resource, jeśli masz listę Image w Resource
    return GameItem(
        label = this.name,
        imageRes = R.drawable.placeholder // lub przypisz konkretny resource image
    )
}
