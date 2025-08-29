@file:JvmName("HabitDataKt")

package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.AlertDialog
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Button
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ButtonDefaults
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Checkbox
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.IconButton
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.MaterialTheme
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.OutlinedTextField
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Scaffold
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TextButton
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TextField
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TextFieldDefaults
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.Calendar
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Компонент аналоговых часов для отображения выбранного времени
 * @param hour - часы (0-23)
 * @param minute - минуты (0-59)
 * @param modifier - модификатор для настройки внешнего вида
 */
@Composable
fun AnalogClock(
    hour: Int,
    minute: Int,
    modifier: Modifier = Modifier,
) {
    // Размер холста для отрисовки часов
    val canvasModifier = Modifier.size(120.dp)
    Box(modifier = modifier) {
        Canvas(
            modifier = canvasModifier
        ) {
            // Радиус циферблата с учетом отступов
            val radius = size.minDimension / 2 - 4.dp.toPx()
            // Центральная точка холста
            val center = Offset(size.width / 2, size.height / 2)

            // Фон циферблата - светло-серый круг
            drawCircle(
                color = Color(0xFFF5F5F5),
                radius = radius,
                center = center
            )

            // Обводка циферблата - темно-серая линия
            drawCircle(
                color = Color.LightGray,
                radius = radius,
                center = center,
                style = Stroke(width = 2.dp.toPx())
            )

            // Отрисовка цифер циферблата (1-12)
            for (i in 1..12) {
                // Вычисление угла для каждой цифры (30 градусов между цифрами)
                val angle = (i * 30f - 90f) * PI / 180f // -90 для начала с 12 часов
                // Радиус для размещения цифр
                val textRadius = radius - 16.dp.toPx()
                // Координаты X и Y для каждой цифры
                val x = center.x + textRadius * cos(angle).toFloat()
                val y = center.y + textRadius * sin(angle).toFloat()

                // Отрисовка цифры на холсте
                drawContext.canvas.nativeCanvas.drawText(
                    i.toString(),
                    x,
                    y + 6.dp.toPx(), // коррекция по Y для центрирования текста
                    android.graphics.Paint().apply {
                        textSize = 12.sp.toPx()
                        textAlign = android.graphics.Paint.Align.CENTER
                        color = android.graphics.Color.BLACK
                    }
                )
            }

            // Минутная стрелка - синяя линия
            val minuteAngle = (minute * 6f - 90f) * PI / 180f // 6 градусов на минуту
            val minuteLength = radius - 10.dp.toPx()
            val minuteEnd = Offset(
                center.x + minuteLength * cos(minuteAngle).toFloat(),
                center.y + minuteLength * sin(minuteAngle).toFloat()
            )
            drawLine(
                start = center,
                end = minuteEnd,
                color = Color.Blue,
                strokeWidth = 2.dp.toPx()
            )

            // Часовая стрелка - черная линия, более толстая
            val hourAngle = ((hour % 12) * 30f + minute / 60f * 30f - 90f) * PI / 180f // 30 градусов на час + коррекция на минуты
            val hourLength = radius - 25.dp.toPx()
            val hourEnd = Offset(
                center.x + hourLength * cos(hourAngle).toFloat(),
                center.y + hourLength * sin(hourAngle).toFloat()
            )
            drawLine(
                start = center,
                end = hourEnd,
                color = Color.Black,
                strokeWidth = 3.dp.toPx()
            )

            // Центральная точка оси вращения стрелок
            drawCircle(
                color = Color.Black,
                radius = 4.dp.toPx(),
                center = center
            )
        }
    }
}

// Идентификатор канала уведомлений для трекера привычек
const val channelId = "hab_tracker_channel"

/**
 * Основная активность приложения - точка входа
 * Управляет жизненным циклом приложения и начальной настройкой
 */
class MainActivity : ComponentActivity() {
    // Код запроса разрешения на уведомления
    private val notificationPermissionCode = 1001

    /**
     * Метод вызывается при создании активности
     * Выполняет начальную настройку приложения
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Запрашиваем разрешение на уведомления для Android 13+ (API 33+)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), notificationPermissionCode)
        }

        // Создаем канал уведомлений (требуется для Android 8+)
        createNotificationChannel()
        // Устанавливаем основной контент приложения
        setContent {
            MyApp()
        }
    }

    /**
     * Создание канала уведомлений для Android 8+
     * Необходимо для отображения уведомлений в новых версиях Android
     */
    private fun createNotificationChannel() {
        val name = "Habit Tracker Channel" // Название канала
        val descriptionText = "Channel for habit reminders" // Описание канала
        val importance = NotificationManager.IMPORTANCE_HIGH // Высокий приоритет уведомлений
        // Создаем канал уведомлений с заданными параметрами
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        // Получаем системный менеджер уведомлений и регистрируем канал
        val notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

/**
 * Основной компонент приложения - управляет навигацией между экранами
 * Содержит общее состояние данных приложения
 */
@SuppressLint("MutableCollectionMutableState")
@Composable
fun MyApp() {
    // Текущий отображаемый экран (заставка, логин, основной экран)
    var currentScreen by remember { mutableStateOf(Screen.Splash) }
    // Текущий экран в разделе привычек (выбранные, добавление, ежедневная отметка)
    var habitsScreen by remember { mutableStateOf(HabitsScreenType.Selected) }

    // Общее состояние приложения:
    // Список всех доступных привычек
    var allHabits by remember { mutableStateOf(mutableListOf("Йога", "Работа", "Уборка", "Тренировка", "Занятие рисованием")) }
    // Список выбранных пользователем привычек
    var selectedHabits by remember { mutableStateOf(mutableListOf<String>()) }

    // Эффект для автоматического перехода со SplashScreen на экран логина
    LaunchedEffect(currentScreen) {
        if (currentScreen == Screen.Splash) {
            delay(2000) // Задержка 2 секунды
            currentScreen = Screen.Login
        }
    }

    // Навигация между основными экранами приложения
    when (currentScreen) {
        Screen.Splash -> SplashScreen() // Экран заставки
        Screen.Login -> LoginScreen(onLogin = {
            // При успешной авторизации переходим к основному экрану
            currentScreen = Screen.Habits
            habitsScreen = HabitsScreenType.Selected
        })
        Screen.Habits -> HabitsMainScreen(
            currentHabitsScreen = habitsScreen,
            onScreenChange = { habitsScreen = it },
            allHabits = allHabits,
            selectedHabits = selectedHabits,
            onSelectedHabitsUpdate = { selectedHabits = it }
        )
    }
}

/**
 * Перечисление типов основных экранов приложения
 */
enum class Screen {
    Splash, Login, Habits
}

/**
 * Перечисление типов экранов в разделе привычек
 */
enum class HabitsScreenType {
    Selected, Add, Daily
}

// =====================
// Экран заставки приложения
// =====================

/**
 * Экран заставки - отображается при запуске приложения на 2 секунды
 * Содержит логотип приложения в виде круга с градиентом
 */
@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), // Белый фон экрана
        contentAlignment = Alignment.Center // Центрирование содержимого
    ) {
        Box(
            modifier = Modifier
                .size(100.dp) // Размер логотипа
                .clip(CircleShape) // Круглая форма
                .background(
                    // Радиальный градиент от зеленого к голубому
                    Brush.radialGradient(
                        colors = listOf(Color.Green, Color.Cyan),
                        center = Offset(0f, 0f),
                        radius = 100f
                    )
                ),
            contentAlignment = Alignment.Center // Центрирование текста в логотипе
        ) {
            Text(
                text = "HT", // Аббревиатура Habits Tracker
                color = Color.White, // Белый цвет текста
                fontSize = 32.sp, // Размер шрифта
                fontWeight = FontWeight.Bold, // Жирный шрифт
                fontFamily = FontFamily.SansSerif // Шрифт без засечек
            )
        }
    }
}

// =====================
// Экран авторизации
// =====================

/**
 * Экран авторизации - проверяет введенные логин и пароль
 * @param onLogin - функция обратного вызова при успешной авторизации
 */
@Composable
fun LoginScreen(onLogin: () -> Unit) {
    // Состояния для ввода логина и пароля
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) } // Показывать ли пароль
    var isError by remember { mutableStateOf(false) } // Есть ли ошибка авторизации

    // Корректные данные для входа (жестко заданы для демонстрации)
    val correctLogin = "Ivan"
    val correctPassword = "123"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD1F2D1)), // Светло-зеленый фон
        horizontalAlignment = Alignment.CenterHorizontally, // Горизонтальное центрирование
        verticalArrangement = Arrangement.Center // Вертикальное центрирование
    ) {
        Text(
            text = "Авторизация", // Заголовок экрана
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier.padding(bottom = 24.dp) // Отступ снизу
        )

        // Поле ввода логина
        OutlinedTextField(
            value = login,
            onValueChange = { login = it }, // Обновление состояния при вводе
            label = { Text("Логин") }, // Подсказка в поле
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .width(200.dp)
                .height(65.dp),
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Person") }, // Иконка пользователя
            isError = isError && login != correctLogin // Подсветка ошибки
        )

        Spacer(modifier = Modifier.height(16.dp)) // Вертикальный отступ

        // Поле ввода пароля
        OutlinedTextField(
            value = password,
            onValueChange = { password = it }, // Обновление состояния при вводе
            label = { Text("Пароль") }, // Подсказка в поле
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .width(200.dp)
                .height(65.dp),
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Lock") }, // Иконка замка
            trailingIcon = {
                // Кнопка переключения видимости пароля
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle password visibility"
                    )
                }
            },
            visualTransformation = if (showPassword) {
                VisualTransformation.None // Показывать пароль как есть
            } else {
                PasswordVisualTransformation() // Скрывать пароль точками
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), // Клавиатура для пароля
            isError = isError && password != correctPassword // Подсветка ошибки
        )

        // Сообщение об ошибке авторизации
        if (isError) {
            Text(
                text = "Неправильный логин или пароль",
                color = Color.Red, // Красный цвет для ошибки
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp)) // Вертикальный отступ

        // Кнопка входа
        Button(
            onClick = {
                // Проверка корректности введенных данных
                if (login == correctLogin && password == correctPassword) {
                    isError = false // Сброс ошибки
                    onLogin() // Вызов функции успешной авторизации
                } else {
                    isError = true // Установка флага ошибки
                }
            },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .width(200.dp)
                .height(50.dp),
            enabled = login.isNotEmpty() && password.isNotEmpty() // Кнопка активна только при заполненных полях
        ) {
            Text(text = "Войти") // Текст на кнопке
        }
    }
}

// =====================
// Основной экран привычек с навигационной панелью
// =====================

/**
 * Основной экран приложения с навигационной панелью
 * Содержит верхнюю панель, боковое меню и контент
 */
@Composable
fun HabitsMainScreen(
    currentHabitsScreen: HabitsScreenType, // Текущий отображаемый экран
    onScreenChange: (HabitsScreenType) -> Unit, // Функция смены экрана
    allHabits: MutableList<String>, // Все доступные привычки
    selectedHabits: MutableList<String>, // Выбранные привычки
    onSelectedHabitsUpdate: (MutableList<String>) -> Unit, // Функция обновления выбранных привычек
) {
    val scaffoldState = rememberScaffoldState() // Состояние для управления Scaffold
    val scope = rememberCoroutineScope() // Область видимости для корутин
    val context = LocalContext.current // Контекст приложения

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            // Верхняя панель приложения
            TopAppBar(
                title = { Text("HabTracker") }, // Название приложения
                navigationIcon = {
                    // Кнопка меню для открытия боковой панели
                    IconButton(onClick = {
                        scope.launch {
                            scaffoldState.drawerState.open() // Открытие боковой панели
                        }
                    }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                backgroundColor = Color(0xFFD1F2D1) // Светло-зеленый фон
            )
        },
        drawerContent = {
            // Боковая навигационная панель (меню)
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(Color(0xFFC7E6C7)) // Светло-зеленый фон
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "Меню",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Пункты меню
                DrawerItem("Список привычек") {
                    scope.launch {
                        scaffoldState.drawerState.close()
                        onScreenChange(HabitsScreenType.Selected) // Переход к списку привычек
                    }
                }
                DrawerItem("Добавить привычки") {
                    scope.launch {
                        scaffoldState.drawerState.close()
                        onScreenChange(HabitsScreenType.Add) // Переход к добавлению привычек
                    }
                }
                DrawerItem("Ежедневная отметка") {
                    scope.launch {
                        scaffoldState.drawerState.close()
                        onScreenChange(HabitsScreenType.Daily) // Переход к ежедневной отметке
                    }
                }
            }
        },
        content = { padding ->
            // Основной контент экрана
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding) // Учет высоты верхней панели
            ) {
                // Отображение соответствующего экрана в зависимости от выбора
                when (currentHabitsScreen) {
                    HabitsScreenType.Selected -> SelectedHabitsScreen(
                        selectedHabits = selectedHabits,
                        onRemoveHabit = { habit ->
                            // Удаление привычки из списка выбранных
                            val updatedList = selectedHabits.toMutableList().apply { remove(habit) }
                            onSelectedHabitsUpdate(updatedList)
                        }
                    )
                    HabitsScreenType.Add -> AddHabitsScreen(
                        allHabits = allHabits,
                        selectedHabits = selectedHabits,
                        onSelectedHabitsUpdate = onSelectedHabitsUpdate
                    )
                    HabitsScreenType.Daily -> DailyMarkScreen(
                        selectedHabits = selectedHabits,
                        onToggleHabit = { habit ->
                            // Логика отметки привычки (заглушка)
                        },
                        onSetReminder = { time ->
                            setNotification(time, context) // Установка напоминания
                        }
                    )
                }
            }
        }
    )
}

// =====================
// Экран выбранных привычек
// =====================

/**
 * Экран отображения выбранных пользователем привычек
 * Позволяет удалять привычки из списка
 */
@Composable
fun SelectedHabitsScreen(
    selectedHabits: MutableList<String>, // Список выбранных привычек
    onRemoveHabit: (String) -> Unit, // Функция удаления привычки
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE6F2F6)) // Светло-голубой фон
            .verticalScroll(rememberScrollState()) // Вертикальная прокрутка
            .padding(16.dp)
    ) {
        Text(
            text = "Выбранные привычки:",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Список выбранных привычек
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (selectedHabits.isEmpty()) {
                // Сообщение при пустом списке
                Text(
                    text = "Нет выбранных привычек",
                    modifier = Modifier.padding(vertical = 8.dp),
                    fontSize = 16.sp,
                    color = Color.Red
                )
            } else {
                // Отображение каждой привычки с кнопкой удаления
                selectedHabits.forEach { habit ->
                    HabitItem(
                        habit = habit,
                        onRemove = { onRemoveHabit(habit) }
                    )
                }
            }
        }
    }
}

// =====================
// Экран добавления привычек
// =====================

/**
 * Экран для добавления новых привычек в список выбранных
 * Использует выпадающий список для выбора привычек
 */
@Composable
fun AddHabitsScreen(
    allHabits: MutableList<String>, // Все доступные привычки
    selectedHabits: MutableList<String>, // Уже выбранные привычки
    onSelectedHabitsUpdate: (MutableList<String>) -> Unit, // Функция обновления списка
) {
    var expandedHabit by remember { mutableStateOf<String?>(null) } // Состояние раскрытого списка
    var selectedHabit by remember { mutableStateOf<String?>(null) } // Выбранная привычка

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF3E0)) // Светло-оранжевый фон
            .verticalScroll(rememberScrollState()) // Вертикальная прокрутка
            .padding(16.dp)
    ) {
        // Заголовок
        Text(
            text = "Выберите привычку:",
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Выбранная привычка или кнопка для открытия списка
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color.White) // Белый фон поля
                .clickable { expandedHabit = if (expandedHabit == "habits") null else "habits" } // Переключение списка
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedHabit ?: "Выберите привычку", // Текст поля
                    modifier = Modifier.weight(1f),
                    fontSize = 16.sp,
                    color = if (selectedHabit == null) Color.Gray else Color.Black
                )
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Expand") // Иконка раскрытия
            }
        }

        // Список привычек (раскрывается при клике)
        if (expandedHabit == "habits") {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEEEEEE)) // Светло-серый фон списка
            ) {
                allHabits.forEach { habit ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                            .clickable {
                                selectedHabit = habit // Выбор привычки
                                expandedHabit = null // Закрытие списка
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = habit,
                            modifier = Modifier.weight(1f),
                            fontSize = 16.sp
                        )
                        if (selectedHabit == habit) {
                            Icon(Icons.Default.Check, contentDescription = "Selected") // Иконка выбора
                        }
                    }
                }
            }
        }

        // Кнопка добавления выбранной привычки
        Button(
            onClick = {
                if (selectedHabit != null && selectedHabit !in selectedHabits) {
                    // Добавление привычки в список выбранных
                    val updatedSelected = selectedHabits.toMutableList().apply { add(selectedHabit!!) }
                    onSelectedHabitsUpdate(updatedSelected)
                    selectedHabit = null // Сброс выбора
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .padding(vertical = 16.dp),
            enabled = selectedHabit != null && selectedHabit !in selectedHabits // Кнопка активна при новой привычке
        ) {
            Text(text = "Добавить") // Текст кнопки
        }
    }
}

// =====================
// Экран ежедневной отметки
// =====================

/**
 * Экран для ежедневной отметки выполнения привычек
 * Содержит часы, выбор времени напоминания и список привычек с чекбоксами
 */
@SuppressLint("DefaultLocale")
@Composable
fun DailyMarkScreen(
    selectedHabits: List<String>, // Список привычек для отметки
    onToggleHabit: (String) -> Unit, // Функция переключения состояния привычки
    onSetReminder: (LocalTime) -> Unit, // Функция установки напоминания
) {
    var showDialog by remember { mutableStateOf(false) } // Состояние диалога выбора времени
    var selectedTime by remember { mutableStateOf(LocalTime.of(12, 0)) } // Выбранное время (по умолчанию 12:00)

    // Состояние отмеченных привычек
    var checkedHabits by remember { mutableStateOf<Set<String>>(setOf()) }

    // История отметок (временно пустая, для будущей реализации)
    var history by remember { mutableStateOf<List<HabitData>>(emptyList()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8F5E9)) // Светло-зеленый фон
            .verticalScroll(rememberScrollState()) // Вертикальная прокрутка
            .padding(16.dp)
    ) {
        // Заголовок с отображением выбранного времени
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = String.format("%02d:%02d", selectedTime.hour, selectedTime.minute), // Форматирование времени
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                color = Color.White,
                modifier = Modifier
                    .background(Color(0xFF00796B)) // Темно-зеленый фон
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(MaterialTheme.shapes.medium) // Скругленные углы
                    .clickable { showDialog = true } // Открытие диалога при клике
            )
        }

        // Аналоговые часы для визуального отображения времени
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(16.dp)
                .border(1.dp, Color.LightGray) // Рамка вокруг часов
                .clickable { showDialog = true } // Открытие диалога при клике
        ) {
            AnalogClock(
                hour = selectedTime.hour,
                minute = selectedTime.minute,
                modifier = Modifier.align(Alignment.Center) // Центрирование часов
            )
        }

        // Кнопка установки напоминания
        Button(
            onClick = {
                onSetReminder(selectedTime) // Вызов функции установки напоминания
            },
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF7C4DFF)) // Фиолетовый цвет
        ) {
            Text(text = "Установить напоминание", color = Color.White)
        }

        // Диалог выбора времени
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false }, // Закрытие диалога
                title = { Text("Выберите время") }, // Заголовок диалога
                text = {
                    TimePicker(
                        selectedTime.hour,
                        selectedTime.minute,
                        { hour -> selectedTime = selectedTime.withHour(hour) }, // Обновление часов
                        { minute -> selectedTime = selectedTime.withMinute(minute) } // Обновление минут
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog = false // Подтверждение выбора
                        }
                    ) {
                        Text("Установить")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDialog = false } // Отмена выбора
                    ) {
                        Text("Отмена")
                    }
                }
            )
        }

        // Список привычек с чекбоксами для отметки выполнения
        if (selectedHabits.isEmpty()) {
            Text(
                text = "Нет привычек для отметки",
                modifier = Modifier.padding(vertical = 8.dp),
                fontSize = 16.sp,
                color = Color.Gray
            )
        } else {
            selectedHabits.forEach { habit ->
                var isChecked by remember(habit) { mutableStateOf(habit in checkedHabits) }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(Color(0xFFD1F2D1)) // Светло-зеленый фон элемента
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = habit,
                        modifier = Modifier.weight(1f), // Занимает всё доступное пространство
                        fontSize = 16.sp
                    )
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { newChecked ->
                            isChecked = newChecked
                            // Обновление множества отмеченных привычек
                            checkedHabits = if (newChecked) {
                                checkedHabits + habit
                            } else {
                                checkedHabits - habit
                            }
                            onToggleHabit(habit) // Вызов внешней функции обработки
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // История отметок
        Text(
            text = "История отметок:",
            modifier = Modifier.padding(bottom = 8.dp),
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )

        // Отображение истории (пока заглушка)
        if (history.isEmpty()) {
            Text(
                text = "Нет данных за предыдущие дни",
                modifier = Modifier.padding(bottom = 8.dp),
                color = Color.Gray
            )
        } else {
            history.forEach { habitData ->
                Text(
                    text = "${habitData.habitName}: ${habitData.completedDays.size} дней",
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = Color.Black
                )
            }
        }
    }
}

// =====================
// Элемент меню навигационной панели
// =====================

/**
 * Элемент навигационного меню
 * @param title - текст элемента меню
 * @param onClick - функция обработки клика
 */
@Composable
fun DrawerItem(title: String, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = title)
    }
}

// =====================
// Элемент списка привычек
// =====================

/**
 * Элемент списка привычек с кнопкой удаления
 * @param habit - название привычки
 * @param onRemove - функция удаления привычки
 */
@Composable
fun HabitItem(habit: String, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color(0xFFD1F2D1)) // Светло-зеленый фон
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = habit,
            modifier = Modifier.weight(1f), // Занимает всё доступное пространство
            fontSize = 16.sp
        )
        IconButton(onClick = onRemove) {
            Icon(Icons.Default.Delete, contentDescription = "Delete") // Иконка удаления
        }
    }
}

// =====================
// Компонент выбора времени (упрощённый)
// =====================

/**
 * Компонент выбора времени с двумя спиннерами
 * @param initialHour - начальные часы
 * @param initialMinute - начальные минуты
 * @param onHourChange - функция обновления часов
 * @param onMinuteChange - функция обновления минут
 */
@Composable
fun TimePicker(
    initialHour: Int,
    initialMinute: Int,
    onHourChange: (Int) -> Unit,
    onMinuteChange: (Int) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Спinner выбора часов
            NumberSpinner(
                value = initialHour,
                onValueChange = onHourChange,
                range = 0..23, // Диапазон часов 0-23
                label = "Час"
            )
            Text(":", fontSize = 24.sp, modifier = Modifier.padding(horizontal = 8.dp)) // Разделитель
            // Спinner выбора минут
            NumberSpinner(
                value = initialMinute,
                onValueChange = onMinuteChange,
                range = 0..59, // Диапазон минут 0-59
                label = "Минута"
            )
        }
    }
}

/**
 * Спinner для выбора числового значения
 * @param value - текущее значение
 * @param onValueChange - функция обновления значения
 * @param range - допустимый диапазон значений
 * @param label - подпись к спиннеру
 */
@Composable
fun NumberSpinner(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange,
    label: String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, style = MaterialTheme.typography.caption) // Подпись
        TextField(
            value = value.toString().padStart(2, '0'), // Форматирование с ведущими нулями
            onValueChange = { newValue ->
                newValue.toIntOrNull()?.let { if (it in range) onValueChange(it) } // Проверка диапазона
            },
            modifier = Modifier
                .width(80.dp)
                .padding(4.dp),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent // Прозрачный фон
            )
        )
    }
}

// =====================
// Логика работы с уведомлениями
// =====================

/**
 * Установка уведомления на заданное время
 * @param time - время срабатывания уведомления
 * @param context - контекст приложения
 */
private fun setNotification(time: LocalTime, context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager // Менеджер будильников
    val intent = Intent(context, NotificationReceiver::class.java).apply {
        putExtra("notification_message", "Пора отметить привычки!") // Сообщение уведомления
    }
    // Создание PendingIntent для доставки уведомления
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Настройка времени срабатывания
    val calendar = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
        set(Calendar.HOUR_OF_DAY, time.hour)
        set(Calendar.MINUTE, time.minute)
        set(Calendar.SECOND, 0)
    }

    // Если выбранное время уже прошло сегодня, установим на завтра
    if (calendar.timeInMillis <= System.currentTimeMillis()) {
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }

    // Установка повторяющегося будильника (ежедневно)
    alarmManager.setRepeating(
        AlarmManager.RTC_WAKEUP, // Будильник срабатывает даже при спящем устройстве
        calendar.timeInMillis,
        AlarmManager.INTERVAL_DAY, // Интервал повторения - один день
        pendingIntent
    )
}

// =====================
// Preview компонентов для предварительного просмотра в IDE
// =====================

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(onLogin = {})
}

@Preview(showBackground = true)
@Composable
fun HabitsMainScreenPreview() {
    HabitsMainScreen(
        currentHabitsScreen = HabitsScreenType.Selected,
        onScreenChange = {},
        allHabits = mutableListOf("Йога", "Работа", "Уборка"),
        selectedHabits = mutableListOf("Уборка", "Йога"),
        onSelectedHabitsUpdate = {}
    )
}