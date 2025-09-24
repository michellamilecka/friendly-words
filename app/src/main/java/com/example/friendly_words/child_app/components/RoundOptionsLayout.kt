package com.example.friendly_words.child_app.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.times
import kotlin.math.ceil
import kotlin.math.min

@Composable
fun RoundOptionsLayout(
    options: List<OptionData>,
    numberOfItems: Int, // faktyczna liczba elementów do wyświetlenia
    isDimmed: (OptionData) -> Boolean,
    isScaled: (OptionData) -> Boolean,
    animateCorrect: (OptionData) -> Boolean,
    outlineCorrect: (OptionData) -> Boolean,
    onClick: (OptionData) -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val spacing = 24.dp
        val horizontalPadding = 24.dp
        val verticalPadding = 24.dp

        // liczba elementów, które faktycznie wyświetlimy
        val displayItems = options.take(numberOfItems)

        // liczba kolumn: max 3
        val columns = min(3, numberOfItems)
        val rows = ceil(numberOfItems / 3.0).toInt()

        // dynamiczny rozmiar obrazka w zależności od dostępnego miejsca
        val itemWidth = (maxWidth - horizontalPadding * 2 - spacing * (columns - 1)) / columns
        val itemHeight = (maxHeight - verticalPadding * 2 - spacing * (rows - 1)) / rows
        val itemSize = min(itemWidth, itemHeight)

        // podział na rzędy
        val rowsList = displayItems.chunked(columns)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding, vertical = verticalPadding)
        ) {
            rowsList.forEach { rowItems ->
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
                            size = itemSize, // cała kratka, razem z ramką i paddingiem
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

data class OptionData(
    val imageRes: Int,
    val label: String
)
