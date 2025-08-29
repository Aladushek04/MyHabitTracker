package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

/**
 * Класс-приемник широковещательных сообщений для отправки уведомлений
 * Наследуется от BroadcastReceiver и обрабатывает события AlarmManager
 */
class NotificationReceiver : BroadcastReceiver() {

    /**
     * Метод вызывается при получении широковещательного сообщения
     * @param context - контекст приложения
     * @param intent - намерение, содержащее данные для уведомления
     */
    override fun onReceive(context: Context, intent: Intent) {
        // Уникальный идентификатор уведомления (используется для обновления/отмены)
        val notificationId = 1
        // Идентификатор канала уведомлений (должен совпадать с channelId в MainActivity)
        val channelId = "hab_tracker_channel"

        // Получение текста сообщения из намерения или использование сообщения по умолчанию
        val message = intent.getStringExtra("notification_message") ?: "Пора отметить привычки!"

        // Создание строителя уведомления с настройками
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Иконка уведомления (стандартная Android иконка)
            .setContentTitle("HabTracker") // Заголовок уведомления
            .setContentText(message) // Текст уведомления
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Высокий приоритет отображения
            .setAutoCancel(true) // Автоматическое закрытие уведомления при нажатии

        // Проверяем, разрешены ли уведомления пользователем
        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            try {
                // Отправка уведомления через NotificationManager
                with(NotificationManagerCompat.from(context)) {
                    notify(notificationId, builder.build()) // Отправка уведомления с указанным ID
                }
            } catch (e: SecurityException) {
                // Обработка исключения в случае отсутствия разрешения на отправку уведомлений
                Log.e("NotificationReceiver", "Нет разрешения на отправку уведомлений", e)
            }
        } else {
            // Логирование ситуации, когда пользователь отключил уведомления
            Log.w("NotificationReceiver", "Уведомления отключены пользователем")
        }
    }
}