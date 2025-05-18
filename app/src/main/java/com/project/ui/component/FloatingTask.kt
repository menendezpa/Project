package com.project.ui.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.project.data.Task
import com.project.data.Urgency


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FloatingTask(
    task: Task,
    onDismiss: () -> Unit,
    onSave: (Task) -> Unit,
    modifier: Modifier = Modifier,
    urgencyLevels: List<Urgency>,
) {
    var taskName by remember { mutableStateOf(task.taskName) }
    var description by remember { mutableStateOf(task.description) }
    var urgency by remember { mutableStateOf(task.urgency) }
    var date by remember { mutableStateOf(task.date) }
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false) // para poder controlar el ancho
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .widthIn(max = 480.dp), // ancho máximo razonable
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                NotesInputField(
                    taskName = taskName,
                    description = description,
                    onTaskNameChange = { taskName = it },
                    onDescriptionChange = { description = it })

                HorizontalPager(
                    state =pagerState,
                    modifier = Modifier // limita altura del pager para no crecer demasiado
                ) { page ->
                    Column {
                        CustomPagerIndicator(currentPage = pagerState.currentPage, pageCount = 2)
                        when (page) {
                            0 -> FirstPage(
                                description = description,
                                onDescriptionChange = { description = it },
                                urgencyLevels = urgencyLevels,
                                selectedUrgency = urgency,
                                onUrgencyChange = { urgency = it },
                                selectedDate = date,
                                onDateSelected = { date = it })
                            // Aquí puedes añadir otras páginas si quieres
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        onSave(
                            task.copy(
                                taskName = taskName,
                                description = description,
                                urgency = urgency,
                                date = date
                            )
                        )
                    }) {
                        Text("Guardar")
                    }
                }
            }
        }
    }

}
@Composable
fun CustomPagerIndicator(currentPage: Int, pageCount: Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        repeat(pageCount) { index ->
            val isSelected = index == currentPage
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(if (isSelected) 12.dp else 8.dp)
                    .background(
                        color = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        shape = CircleShape
                    )
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FirstPage(
    description: String,
    onDescriptionChange: (String) -> Unit,
    urgencyLevels: List<Urgency>,
    selectedUrgency: Urgency?,
    onUrgencyChange: (Urgency) -> Unit,
    selectedDate: String,
    onDateSelected: (String) -> Unit,
) {
    Column (
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            UrgencyDropDown(
                modifier = Modifier.weight(1f),
                urgencyLevels = urgencyLevels,
                selectedUrgency = selectedUrgency,
                onUrgencyChange = onUrgencyChange
            )
            DatePickerField(
                modifier = Modifier.weight(1f),
                selectedDate = selectedDate,
                onDateSelected = onDateSelected
            )
        }
        DescriptionInputField(
            description = description, onDescriptionChange = { onDescriptionChange(it) })
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun FloatingTaskPreview2() {
    val task = Task(
        id = "1", taskName = "Task 1", description = "Description 1", date = "2025-05-01"
    )
    FloatingTask(
        task = task,
        onDismiss = {},
        onSave = {},
        urgencyLevels = List(3) { Urgency(name = "Urgencia $it") })
}