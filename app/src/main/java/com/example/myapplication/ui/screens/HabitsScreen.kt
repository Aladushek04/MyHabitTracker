// ui/screens/HabitsScreen.kt
package com.example.myapplication.ui.screens

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.HabitsScreenType
import com.example.myapplication.data.HabitData
import com.example.myapplication.ui.components.AnalogClock
import com.example.myapplication.ui.components.DrawerItem
import com.example.myapplication.ui.components.HabitItem
import com.example.myapplication.ui.components.TimePicker
import com.example.myapplication.utils.setNotification // Убедитесь, что этот импорт есть
import kotlinx.coroutines.launch
import java.time.LocalTime

// HabitsMainScreen
@Composable
fun HabitsMainScreen(
    currentHabitsScreen: HabitsScreenType,
    onScreenChange: (HabitsScreenType) -> Unit,
    onBackToMain: () -> Unit,
    allHabits: MutableList<String>,
    selectedHabits: MutableList<String>,
    onAddHabit: (String) -> Unit,
    onToggleSelectedHabit: (String) -> Unit,
    onRemoveHabit: (String) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("HabTracker") },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                backgroundColor = Color(0xFFD1F2D1)
            )
        },
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(Color(0xFFC7E6C7))
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "Меню",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                DrawerItem("Список привычек") {
                    scope.launch {
                        scaffoldState.drawerState.close()
                        onScreenChange(HabitsScreenType.Selected)
                    }
                }
                DrawerItem("Добавить привычки") {
                    scope.launch {
                        scaffoldState.drawerState.close()
                        onScreenChange(HabitsScreenType.Add)
                    }
                }
                DrawerItem("Ежедневная отметка") {
                    scope.launch {
                        scaffoldState.drawerState.close()
                        onScreenChange(HabitsScreenType.Daily)
                    }
                }
            }
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                when (currentHabitsScreen) {
                    HabitsScreenType.Selected -> SelectedHabitsScreen(
                        selectedHabits = selectedHabits,
                        onRemoveHabit = onRemoveHabit
                    )
                    HabitsScreenType.Add -> AddHabitsScreen(
                        allHabits = allHabits,
                        selectedHabits = selectedHabits,
                        onAddHabit = onAddHabit,
                        onSelectHabit = onToggleSelectedHabit // <-- Передаем правильный колбэк
                    )
                    HabitsScreenType.Daily -> DailyMarkScreen(
                        selectedHabits = selectedHabits.toList(),
                        onToggleHabit = onToggleSelectedHabit,
                        onSetReminder = { time ->
                            setNotification(time, context) // <-- Вызываем функцию из utils
                        }
                    )
                }
            }
        }
    )
}

// SelectedHabitsScreen
@Composable
fun SelectedHabitsScreen(
    selectedHabits: MutableList<String>,
    onRemoveHabit: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE6F2F6))
            .verticalScroll(rememberScrollState())
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
                Text(
                    text = "Нет выбранных привычек",
                    modifier = Modifier.padding(vertical = 8.dp),
                    fontSize = 16.sp,
                    color = Color.Red
                )
            } else {
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

// AddHabitsScreen
@Composable
fun AddHabitsScreen(
    allHabits: MutableList<String>,
    selectedHabits: MutableList<String>,
    onAddHabit: (String) -> Unit,
    onSelectHabit: (String) -> Unit // <-- Используем правильный колбэк
) {
    var expandedHabit by remember { mutableStateOf<String?>(null) }
    var selectedHabit by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF3E0))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Выпадающий список привычек
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
                .background(Color.White)
                .clickable { expandedHabit = if (expandedHabit == "habits") null else "habits" }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedHabit ?: "Выберите привычку",
                    modifier = Modifier.weight(1f),
                    fontSize = 16.sp,
                    color = if (selectedHabit == null) Color.Gray else Color.Black
                )
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Expand")
            }
        }

        // Список привычек (раскрывается при клике)
        if (expandedHabit == "habits") {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEEEEEE))
            ) {
                allHabits.forEach { habit ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                            .clickable {
                                selectedHabit = habit
                                expandedHabit = null
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = habit,
                            modifier = Modifier.weight(1f),
                            fontSize = 16.sp
                        )
                        if (selectedHabit == habit) {
                            Icon(Icons.Default.Check, contentDescription = "Selected")
                        }
                    }
                }
            }
        }

        Button(
            onClick = {
                // При нажатии добавляем выбранную привычку в список выбранных
                // Проверяем, что привычка выбрана и еще не в списке
                if (selectedHabit != null && selectedHabit !in selectedHabits) {
                    // Вызываем колбэк onSelectHabit, который должен добавить привычку
                    // в ViewModel или обновить состояние
                    onSelectHabit(selectedHabit!!)
                    // Очищаем выбор после добавления
                    selectedHabit = null
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .padding(vertical = 16.dp),
            enabled = selectedHabit != null && selectedHabit !in selectedHabits
        ) {
            Text(text = "Добавить")
        }
    }
}

// DailyMarkScreen
@Composable
fun DailyMarkScreen(
    selectedHabits: List<String>,
    onToggleHabit: (String) -> Unit,
    onSetReminder: (LocalTime) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf(LocalTime.of(12, 0)) } // Время по умолчанию 12:00

    // Состояние отмеченных привычек
    var checkedHabits by remember { mutableStateOf<Set<String>>(setOf()) }

    // История отметок (временно пустая, позже будем загружать)
    var history by remember { mutableStateOf<List<HabitData>>(emptyList()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8F5E9))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Заголовок с временем
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = String.format("%02d:%02d", selectedTime.hour, selectedTime.minute),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .background(Color(0xFF00796B))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .clickable { showDialog = true }
            )
        }

        // Часы (визуальный элемент)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(16.dp)
                .border(1.dp, Color.LightGray)
                .clickable { showDialog = true }
        ) {
            AnalogClock(
                hour = selectedTime.hour,
                minute = selectedTime.minute,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Кнопка установки напоминания
        Button(
            onClick = {
                onSetReminder(selectedTime)
            },
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF7C4DFF))
        ) {
            Text(text = "Установить напоминание", color = Color.White)
        }

        // Диалог выбора времени
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Выберите время") },
                text = {
                    TimePicker(
                        selectedTime.hour,
                        selectedTime.minute,
                        { hour -> selectedTime = selectedTime.withHour(hour) },
                        { minute -> selectedTime = selectedTime.withMinute(minute) }
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog = false
                        }
                    ) {
                        Text("Установить")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDialog = false }
                    ) {
                        Text("Отмена")
                    }
                }
            )
        }

        // Список привычек с чекбоксами
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
                        .background(Color(0xFFD1F2D1))
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = habit,
                        modifier = Modifier.weight(1f),
                        fontSize = 16.sp
                    )
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { newChecked ->
                            isChecked = newChecked
                            checkedHabits = if (newChecked) {
                                checkedHabits + habit
                            } else {
                                checkedHabits - habit
                            }
                            // Вызываем внешнюю функцию для обработки
                            onToggleHabit(habit)
                            // Здесь же можно вызвать saveHabitHistory
                            // saveHabitHistory(habit, newChecked, LocalContext.current)
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

        // Отображение истории (пример)
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