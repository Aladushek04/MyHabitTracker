package com.example.myapplication.data

import java.time.LocalDate

data class HabitData(
    val habitName: String,
    val completedDays: List<LocalDate> // Список дат, когда была отмечена привычка
)