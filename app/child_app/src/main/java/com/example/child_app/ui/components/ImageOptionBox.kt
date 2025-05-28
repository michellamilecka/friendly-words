package com.example.child_app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.child_app.ui.theme.YellowFrames


@Composable
fun ImageOptionBox(
    imageRes: Int,
    label: String,
    size: Dp,
    isDimmed: Boolean = false,
    onClick: () -> Unit = {}
) {
    val dimColor = if (isDimmed) Color.Black.copy(alpha = 0.4f) else Color.Transparent
    val framePadding = 12.dp // Odstęp ramki od białego pola

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(size + framePadding * 2)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier.size(size + framePadding * 2)
        ) {
            // Przerywana ramka
            Canvas(modifier = Modifier.matchParentSize()) {
                drawRoundRect(
                    color = YellowFrames,
                    size = Size(this.size.width, this.size.height),
                    style = Stroke(
                        width = 4.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                    ),
                    cornerRadius = CornerRadius(32.dp.toPx())
                )
            }

            // Biały kwadrat z obrazkiem i tekstem w środku
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .size(size)
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .padding(12.dp) // wewnętrzne paddingi
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = label,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f) // zajmuje całą możliwą wysokość oprócz tekstu
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = label,
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Warstwa wyszarzenia
            Box(
                modifier = Modifier
                    .size(size)
                    .align(Alignment.Center)
                    .background(dimColor)
            )
        }
    }
}






