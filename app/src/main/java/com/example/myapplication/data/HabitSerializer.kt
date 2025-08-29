package com.example.myapplication.data

import androidx.datastore.core.Serializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.builtins.ListSerializer // <-- Важный импорт
import java.io.InputStream
import java.io.OutputStream

object HabitSerializer : Serializer<List<Habit>> {
    override val defaultValue: List<Habit> = emptyList()

    override suspend fun readFrom(input: InputStream): List<Habit> {
        try {
            // Явно указываем десериализатор для List<Habit>
            return Json.decodeFromString(
                deserializer = ListSerializer(Habit.serializer()),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            // Обработка ошибок десериализации
            e.printStackTrace()
            return defaultValue
        }
    }

    override suspend fun writeTo(t: List<Habit>, output: OutputStream) {
        // Явно указываем сериализатор для List<Habit>
        output.write(
            Json.encodeToString(
                serializer = ListSerializer(Habit.serializer()),
                value = t
            ).encodeToByteArray()
        )
    }
}