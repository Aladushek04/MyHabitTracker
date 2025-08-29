package com.example.myapplication.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
// import com.example.myapplication.R // Удалите эту строку, если иконка не используется или замените на стандартную

class NotificationReceiver : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = 1
        val channelId = "hab_tracker_channel"

        val message = intent.getStringExtra("notification_message") ?: "Пора отметить привычки!"

        // Используем стандартную иконку Android
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Стандартная иконка
            .setContentTitle("HabTracker") // Исправлена ошибка: setContentTitle
            .setContentText(message) // Исправлена ошибка: setContentText
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build()) // Исправлена ошибка: notify
        }
    }
}