package com.example.myapplication

import java.time.LocalDate

// =====================
// Вспомогательный класс для хранения данных о привычках (для будущей реализации)
// =====================

/**
 * Класс для хранения данных о привычке и её истории выполнения
 * @property habitName - название привычки
 * @property completedDays - список дней, когда привычка была выполнена
 */

data class HabitData(
    val habitName: String,
    val completedDays: List<LocalDate> // Список дат, когда была отмечена привычка
)