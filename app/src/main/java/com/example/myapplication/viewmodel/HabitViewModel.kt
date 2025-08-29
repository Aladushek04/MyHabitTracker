package com.example.myapplication

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Habit
import com.example.myapplication.datastore.habitDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HabitViewModel(private val context: Context) : ViewModel() {

    // Состояние привычек из DataStore
    val habits: StateFlow<List<Habit>> = context.habitDataStore.data
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf(
                Habit("Йога"),
                Habit("Работа"),
                Habit("Уборка"),
                Habit("Тренировка"),
                Habit("Занятие рисованием")
            )
        )

    // Получаем список всех привычек
    val allHabits = mutableStateListOf<String>()
        .apply {
            habits.value.map { it.name }.toCollection(this)
        }

    // Получаем список выбранных привычек
    val selectedHabits = mutableStateListOf<String>()
        .apply {
            habits.value.filter { it.isSelected }.map { it.name }.toCollection(this)
        }

    // Инициализация списков при запуске ViewModel
    init {
        viewModelScope.launch {
            habits.collect { habitList ->
                allHabits.clear()
                allHabits.addAll(habitList.map { it.name })

                selectedHabits.clear()
                selectedHabits.addAll(habitList.filter { it.isSelected }.map { it.name })
            }
        }
    }

    // Добавить новую привычку
    fun addNewHabit(habitName: String) {
        if (habitName !in allHabits) {
            viewModelScope.launch {
                val currentHabits = habits.value
                val newHabit = Habit(habitName)
                val updatedHabits = currentHabits + newHabit
                context.habitDataStore.updateData { updatedHabits }
            }
        }
    }

    // Добавить/удалить привычку из выбранных
    fun toggleSelectedHabit(habitName: String) {
        viewModelScope.launch {
            val currentHabits = habits.value
            val updatedHabits = currentHabits.map { habit ->
                if (habit.name == habitName) {
                    habit.copy(isSelected = !habit.isSelected)
                } else {
                    habit
                }
            }
            context.habitDataStore.updateData { updatedHabits }
        }
    }

    // Удалить привычку
    fun removeHabit(habitName: String) {
        viewModelScope.launch {
            val currentHabits = habits.value
            val updatedHabits = currentHabits.filter { it.name != habitName }
            context.habitDataStore.updateData { updatedHabits }
        }
    }
}