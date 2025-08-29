package com.example.myapplication.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnalogClock(
    hour: Int,
    minute: Int,
    modifier: Modifier = Modifier
) {
    val canvasModifier = Modifier.size(120.dp)
    Box(modifier = modifier) {
        Canvas(
            modifier = canvasModifier
        ) {
            val radius = size.minDimension / 2 - 4.dp.toPx()
            val center = Offset(size.width / 2, size.height / 2)

            // Фон циферблата
            drawCircle(
                color = Color(0xFFF5F5F5),
                radius = radius,
                center = center
            )

            // Обводка циферблата
            drawCircle(
                color = Color.LightGray,
                radius = radius,
                center = center,
                style = Stroke(width = 2.dp.toPx())
            )

            // Часы (цифры от 1 до 12)
            for (i in 1..12) {
                val angle = (i * 30f - 90f) * PI / 180f // -90 для начала с 12 часов
                val textRadius = radius - 16.dp.toPx()
                val x = center.x + textRadius * cos(angle).toFloat()
                val y = center.y + textRadius * sin(angle).toFloat()

                // Рисуем цифры
                drawContext.canvas.nativeCanvas.drawText(
                    i.toString(),
                    x,
                    y + 6.dp.toPx(), // коррекция по Y для центрирования текста
                    android.graphics.Paint().apply {
                        textSize = 12.sp.toPx()
                        textAlign = android.graphics.Paint.Align.CENTER
                        color = android.graphics.Color.BLACK
                    }
                )
            }

            // Минутная стрелка
            val minuteAngle = (minute * 6f - 90f) * PI / 180f
            val minuteLength = radius - 10.dp.toPx()
            val minuteEnd = Offset(
                center.x + minuteLength * cos(minuteAngle).toFloat(),
                center.y + minuteLength * sin(minuteAngle).toFloat()
            )
            drawLine(
                start = center,
                end = minuteEnd,
                color = Color.Blue,
                strokeWidth = 2.dp.toPx()
            )

            // Часовая стрелка
            val hourAngle = ((hour % 12) * 30f + minute / 60f * 30f - 90f) * PI / 180f
            val hourLength = radius - 25.dp.toPx()
            val hourEnd = Offset(
                center.x + hourLength * cos(hourAngle).toFloat(),
                center.y + hourLength * sin(hourAngle).toFloat()
            )
            drawLine(
                start = center,
                end = hourEnd,
                color = Color.Black,
                strokeWidth = 3.dp.toPx()
            )

            // Центральная точка
            drawCircle(
                color = Color.Black,
                radius = 4.dp.toPx(),
                center = center
            )
        }
    }
}