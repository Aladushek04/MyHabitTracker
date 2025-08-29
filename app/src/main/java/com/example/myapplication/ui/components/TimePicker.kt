package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.*
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TextField
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TextFieldDefaults
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.MaterialTheme
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TimePicker(
    initialHour: Int,
    initialMinute: Int,
    onHourChange: (Int) -> Unit,
    onMinuteChange: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Выбор часов
            NumberSpinner(
                value = initialHour,
                onValueChange = onHourChange,
                range = 0..23,
                label = "Час"
            )
            Text(":", fontSize = 24.sp, modifier = Modifier.padding(horizontal = 8.dp))
            // Выбор минут
            NumberSpinner(
                value = initialMinute,
                onValueChange = onMinuteChange,
                range = 0..59,
                label = "Минута"
            )
        }
    }
}

@Composable
fun NumberSpinner(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, style = MaterialTheme.typography.caption)
        TextField(
            value = value.toString().padStart(2, '0'),
            onValueChange = { newValue ->
                newValue.toIntOrNull()?.let { if (it in range) onValueChange(it) }
            },
            modifier = Modifier
                .width(80.dp)
                .padding(4.dp),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent
            )
        )
    }
}