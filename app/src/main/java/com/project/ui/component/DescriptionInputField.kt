package com.project.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
/**
 * Campo de texto para la descripción.
 * @param description Descripción del evento.
 * @param onDescriptionChange Función para manejar cambios en la descripción.
 * @see TextField
 * */
@Composable
fun DescriptionInputField(description: String, onDescriptionChange: (String) -> Unit) {
    TextField(
        value = description,
        onValueChange = onDescriptionChange,
        label = { Text("Descripción") },
        modifier = Modifier.fillMaxWidth()
    )
}