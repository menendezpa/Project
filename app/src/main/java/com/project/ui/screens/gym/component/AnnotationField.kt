package com.project.ui.screens.gym.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AnnotationField(
    annotation: String,
    onAnnotationChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = annotation,
        onValueChange = onAnnotationChange,
        label = { Text("Anotaciones") },
        placeholder = { Text("Escribe tus notas o detalles aquí...") },
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp),  // Altura personalizada
        maxLines = 10,
        singleLine = false
    )
}