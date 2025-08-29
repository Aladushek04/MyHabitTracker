package com.example.myapplication.data

import kotlinx.serialization.Serializable

@Serializable
data class Habit(
    val name: String,
    val isSelected: Boolean = false
)