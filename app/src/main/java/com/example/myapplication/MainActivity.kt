package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.*
import com.example.myapplication.ui.screens.HabitsMainScreen
import com.example.myapplication.ui.screens.LoginScreen
import com.example.myapplication.ui.screens.SplashScreen
import kotlinx.coroutines.delay

const val channelId = "hab_tracker_channel"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Создание канала уведомлений
        val name = "Habit Tracker Channel"
        val descriptionText = "Channel for habit reminders"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        setContent {
            // Передаем контекст в ViewModel
            val habitViewModel: HabitViewModel = viewModel(factory = HabitViewModelFactory(this))
            MyApp(habitViewModel = habitViewModel)
        }
    }
}

// Перенесите enum классы в отдельный файл, если хотите
enum class Screen {
    Splash, Login, Habits
}

enum class HabitsScreenType {
    Selected, Add, Daily
}

@Composable
fun MyApp(habitViewModel: HabitViewModel) {
    var currentScreen by remember { mutableStateOf(Screen.Splash) }
    var habitsScreen by remember { mutableStateOf(HabitsScreenType.Selected) }

    LaunchedEffect(currentScreen) {
        if (currentScreen == Screen.Splash) {
            delay(2000)
            currentScreen = Screen.Login
        }
    }

    when (currentScreen) {
        Screen.Splash -> SplashScreen()
        Screen.Login -> LoginScreen(onLogin = {
            currentScreen = Screen.Habits
            habitsScreen = HabitsScreenType.Selected
        })
        Screen.Habits -> HabitsMainScreen(
            currentHabitsScreen = habitsScreen,
            onScreenChange = { habitsScreen = it },
            onBackToMain = { currentScreen = Screen.Login },
            allHabits = habitViewModel.allHabits,
            selectedHabits = habitViewModel.selectedHabits,
            onAddHabit = { habitName -> habitViewModel.addNewHabit(habitName) },
            onToggleSelectedHabit = { habitName -> habitViewModel.toggleSelectedHabit(habitName) },
            onRemoveHabit = { habitName -> habitViewModel.removeHabit(habitName) }
        )
    }
}