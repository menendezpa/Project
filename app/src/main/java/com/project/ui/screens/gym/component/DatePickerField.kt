package com.project.ui.screens.gym.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.ZoneId


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    // Mostrar el nuevo DatePickerDialog de Material 3
    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    val millis = datePickerState.selectedDateMillis
                    val localDate = millis?.let {
                        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                    }

                    val formattedDate = localDate?.let {
                        "${it.year}-${it.monthValue.toString().padStart(2, '0')}-${it.dayOfMonth.toString().padStart(2, '0')}"
                    } ?: ""

                    onDateSelected(formattedDate)
                    showDialog = false
                })
                {
                    Text("OK")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Campo visual estilo Material 3
    OutlinedTextField(
        value = selectedDate,
        onValueChange = {},
        readOnly = true,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp) // fuerza altura fija
            .clickable { showDialog = true },
        label = { Text("Fecha") },
        placeholder = { Text("Seleccionar fecha") },
        trailingIcon = {
            Icon(
                imageVector = Icons.Filled.CalendarToday,
                contentDescription = "Selecciona la fecha",
                modifier = Modifier.clickable { showDialog = true }
            )
        },
        singleLine = true,
        maxLines = 1,
        // Opcional: ajustar padding interno si quieres mayor precisión
        colors = TextFieldDefaults.colors(),
    )



}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun DatePickerFieldPreview() {
    var selectedDate by remember { mutableStateOf("") }
    DatePickerField(selectedDate = selectedDate, onDateSelected = { selectedDate = it })
}