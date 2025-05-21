package com.project.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
/**
 * Muestra los botones de acción para una tarea.
 * @param onCancel Función a ejecutar al hacer clic en el botón de cancelar.
 * @param onConfirm Función a ejecutar al hacer clic en el botón de confirmar.
 * @see Row
 * @see TextButton
 * @see Modifier
 * @see Spacer
 * @see Text
 * @see MaterialTheme
 * */
@Composable
fun TaskActionButtons(onCancel: () -> Unit, onConfirm: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextButton(
            modifier = Modifier.weight(1f),
            onClick = onCancel,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Text("Cancelar")
        }
        Spacer(modifier = Modifier.width(8.dp))
        TextButton(
            modifier = Modifier.weight(1f),
            onClick = onConfirm,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Aceptar")
        }
    }
}