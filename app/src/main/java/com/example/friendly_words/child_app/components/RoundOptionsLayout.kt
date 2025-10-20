package com.example.friendly_words.child_app.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.example.friendly_words.child_app.data.GameItem
import kotlin.math.ceil
import kotlin.math.min

@Composable
fun RoundOptionsLayout(
    options: List<GameItem>,
    numberOfItems: Int, // faktyczna liczba elementów do wyświetlenia
    isDimmed: (GameItem) -> Boolean,
    isScaled: (GameItem) -> Boolean,
    animateCorrect: (GameItem) -> Boolean,
    outlineCorrect: (GameItem) -> Boolean,
    showLabels: Boolean = true,
    onClick: (GameItem) -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val spacing = 24.dp
        val horizontalPadding = 24.dp
        val verticalPadding = 24.dp

        val displayItems = options.take(numberOfItems)

        val columns = when (numberOfItems) {
            4 -> 2
            else -> min(3, numberOfItems)
        }
        val rows = when (numberOfItems) {
            4 -> 2
            else -> ceil(numberOfItems / columns.toDouble()).toInt()
        }

        val itemWidth = (maxWidth - horizontalPadding * 2 - spacing * (columns - 1)) / columns
        val itemHeight = (maxHeight - verticalPadding * 2 - spacing * (rows - 1)) / rows
        val itemSize = min(itemWidth, itemHeight)

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
                    horizontalArrangement = Arrangement.spacedBy(spacing, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rowItems.forEach { item ->
                        ImageOptionBox(
                            imagePath = item.imagePath,
                            label = item.label,
                            size = itemSize,
                            isDimmed = isDimmed(item),
                            isScaled = isScaled(item),
                            animateCorrect = animateCorrect(item),
                            outlineCorrect = outlineCorrect(item),
                            showLabel = showLabels,
                            onClick = { onClick(item) }
                        )
                    }
                }
            }
        }
    }
}