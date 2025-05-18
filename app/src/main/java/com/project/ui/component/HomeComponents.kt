package com.project.ui.component

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.data.Place
import com.project.data.Task
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import androidx.compose.foundation.basicMarquee
//Componente de cada celda del calendario
//@Composable
//fun CalendarCell(
//    day: Int,
//    modifier: Modifier = Modifier,
//    onClick: () -> Unit,
//    isWithTask: Boolean = false,
//){
//    Box(
//        modifier = modifier
//            .size(40.dp)
//            .clickable { onClick() }
//            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
//            .background(if (isWithTask) Color.Green else Color.White),
//        ){
//        Text(day.toString() , modifier = Modifier.align(Alignment.Center))
//    }
//}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarHeader(
    modifier: Modifier = Modifier,
    currentMonth: YearMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    // Formateamos el YearMonth a un String con el formato deseado.
    val currentMonthText = currentMonth.format(
        DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())
    )
    // Obtenemos los nombres de los días de la semana en formato corto según la configuración regional.
    val daysOfWeek = DayOfWeek.entries.map {
        it.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // Bloque de navegación entre meses con título central.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),// Usamos el mismo padding que en la rejilla
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPreviousMonth) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Mes anterior",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = currentMonthText.replaceFirstChar { it.uppercaseChar() },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            IconButton(onClick = onNextMonth) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Mes siguiente",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        // Bloque de los días de la semana.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
fun CalendarCell(
    day: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isWithTask: Boolean = false,
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clickable { onClick() }
            .clip(RoundedCornerShape(8.dp))
            .background(
                color = if (isWithTask) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.inversePrimary, shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = if (isWithTask) MaterialTheme.colorScheme.outlineVariant else MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
            ), contentAlignment = Alignment.Center) {
        Text(
            text = day.toString(),
            color = if (isWithTask) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        )
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarGrid(
    yearMonth: YearMonth,
    modifier: Modifier = Modifier,
    onDayClick: (LocalDate) -> Unit, // El callback recibe la fecha completa
    daysWithTask: Set<LocalDate> = emptySet() // Conjunto de fechas que tienen tareas
) {
    // 1. Obtenemos el primer día del mes y el total de días
    val firstDayOfMonth = yearMonth.atDay(1)
    val totalDays = yearMonth.lengthOfMonth()
    // Si la semana inicia en lunes, el índice (0-based) del primer día es:
    val firstDayIndex = firstDayOfMonth.dayOfWeek.value - 1

    // 2. Calculamos el total de celdas para cuadrícula (múltiplo de 7)
    val totalCells = ((firstDayIndex + totalDays + 6) / 7) * 7

    // 3. Creamos una lista que contenga, para cada celda, la fecha completa o null si es celda vacía
    val cellDates: List<LocalDate?> = List(totalCells) { index ->
        if (index < firstDayIndex || index >= firstDayIndex + totalDays) null
        else yearMonth.atDay(index - firstDayIndex + 1)
    }

    // 4. Agrupamos la lista en filas de 7 celdas cada una
    val rows = cellDates.chunked(7)

    Column(modifier = modifier.padding(8.dp)) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
            ) {
                row.forEach { date ->
                    if (date == null) {
                        // Celdas vacías: usamos un Spacer para mantener el tamaño
                        Spacer(modifier = Modifier.size(40.dp))
                    } else {
                        CalendarCell(
                            day = date.dayOfMonth,
                            onClick = { onDayClick(date) },  // Se pasa el objeto LocalDate completo
                            isWithTask = daysWithTask.contains(date),
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskCard(
    task: Task,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Encabezado: nombre + urgencia + estado
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = task.taskName,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                UrgeIndicator(urgency = task.urgency)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Línea divisoria suave
            HorizontalDivider(
                thickness = 0.8.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Lugar y fecha con íconos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = task.place.name.replace(Regex("\\d+,?\\s*"), ""),
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .basicMarquee() // << animación tipo marquesina
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = task.date,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            // Descripción
            if (task.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Estado (pendiente, completada)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = task.state.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .align(Alignment.End)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskList(
    tasks: List<Task>, date: LocalDate, onTaskClick: (Task) -> Unit
) {
    // Filtrado: solo se muestran las tasks cuya fecha coincide exactamente con la fecha seleccionada.
    val filteredTasks = tasks.filter { task ->
        // Imprime en Logcat para depuración (recuerda tener permisos para ver Logcat)
        Log.d(
            "TaskList",
            "Comparando task: ${task.taskName} con fecha ${task.localDate} vs fecha seleccionada: $date"
        )
        task.localDate == date
    }

    if (filteredTasks.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Sin tareas",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "No hay tareas para la fecha ${
                        date.format(
                            DateTimeFormatter.ofPattern(
                                "d MMM uuu", Locale.getDefault()
                            )
                        )
                    }",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "¡Crea una!",
                    modifier = Modifier.padding(8.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold
                    )
                )
            }
        }

    } else {
        AnimatedVisibility(true) {

            LazyColumn {
                items(filteredTasks) { task ->
                    TaskCard(
                        task = task,
                        onClick = { onTaskClick(task) }, // Pasas el click correctamente
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp) // sin clickable aquí
                    )
                }
            }

        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewTaskList() {
    val sampleTasks = listOf(
        Task(
            taskName = "Comprar víveres",
            description = "Lista del supermercado",
            date = LocalDate.of(2025, 5, 1).toString()
        ), Task(
            taskName = "Hacer ejercicio",
            description = "Sesión de gimnasio",
            date = LocalDate.of(2025, 5, 1).toString()
        ), Task(
            taskName = "Leer un libro",
            description = "Capítulo 3",
            date = LocalDate.of(2025, 5, 1).toString()
        )
    )

    MaterialTheme {
        TaskList(
            tasks = sampleTasks, onTaskClick = {}, date = LocalDate.of(2025, 5, 1)
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun TaskPreview() {
    val task =
        Task(
            taskName = "Task 1",
            description = "Description 1",
            date = LocalDate.now().toString(),
            place = Place(name = "19162 Pioz, Guadalajara, España")
        )
    TaskCard(
        modifier = Modifier.fillMaxWidth(), task = task, onClick = { TODO() })
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun CalendarGridPreview() {
    val currentMonth = YearMonth.now()
    CalendarGrid(yearMonth = currentMonth, onDayClick = {})
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun CalendarHeaderPreview() {
    val currentMonth = YearMonth.now()
    CalendarHeader(
        modifier = Modifier,
        currentMonth = currentMonth,
        onPreviousMonth = { currentMonth.minusMonths(1) },
        onNextMonth = { currentMonth.plusMonths(1) })

}


@Preview
@Composable
fun CalendarCellPreview() {

    CalendarCell(day = 1, onClick = {}, isWithTask = true)
}
