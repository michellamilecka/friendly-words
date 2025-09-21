package com.example.friendly_words.child_app.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times

@Composable
fun RoundOptionsLayout(
    options: List<OptionData>,
    numberOfItems: Int, // 1–6
    isDimmed: (OptionData) -> Boolean,
    isScaled: (OptionData) -> Boolean,
    animateCorrect: (OptionData) -> Boolean,
    outlineCorrect: (OptionData) -> Boolean,
    onClick: (OptionData) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Obliczamy dynamiczny rozmiar obrazka w zależności od szerokości ekranu i liczby elementów
        val spacing = 32.dp
        val horizontalPadding = 24.dp
        val totalSpacing = spacing * (minOf(numberOfItems, options.size) - 1)
        val availableWidth = maxWidth - 2 * horizontalPadding - totalSpacing
        val itemSize = availableWidth / minOf(numberOfItems, options.size)

        when (options.size) {
            1 -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    ImageOptionBox(
                        imageRes = options[0].imageRes,
                        label = options[0].label,
                        size = itemSize,
                        isDimmed = isDimmed(options[0]),
                        isScaled = isScaled(options[0]),
                        animateCorrect = animateCorrect(options[0]),
                        outlineCorrect = outlineCorrect(options[0]),
                        onClick = { onClick(options[0]) }
                    )
                }
            }
            2, 3, 4, 5, 6 -> {
                // Tutaj możesz użyć swojej logiki rzędy / kolumny jak wcześniej
                // tylko teraz ImageOptionBox bierze size = itemSize
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(spacing),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Prosta logika: np. dla 5 – 3 na górze, 2 na dole
                    val rows = when (options.size) {
                        2, 3, 4 -> listOf(options)
                        5 -> listOf(options.take(3), options.takeLast(2))
                        6 -> listOf(options.take(3), options.drop(3).take(3))
                        else -> listOf(options)
                    }

                    rows.forEach { rowItems ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(spacing),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Spacer(Modifier.weight(1f))
                            rowItems.forEach { item ->
                                ImageOptionBox(
                                    imageRes = item.imageRes,
                                    label = item.label,
                                    size = itemSize,
                                    isDimmed = isDimmed(item),
                                    isScaled = isScaled(item),
                                    animateCorrect = animateCorrect(item),
                                    outlineCorrect = outlineCorrect(item),
                                    onClick = { onClick(item) }
                                )
                            }
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}


data class OptionData(
    val imageRes: Int,
    val label: String
)
