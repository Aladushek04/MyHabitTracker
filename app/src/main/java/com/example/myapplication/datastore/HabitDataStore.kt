package com.example.myapplication.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.myapplication.data.Habit
import com.example.myapplication.data.HabitSerializer

// Создание DataStore
val Context.habitDataStore: DataStore<List<Habit>> by dataStore(
    fileName = "habits.json",
    serializer = HabitSerializer
)