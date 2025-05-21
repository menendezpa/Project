package com.project.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.project.data.Urgency
/**
 * Muestra un campo de texto para seleccionar una urgencia.
 * @param modifier Modificador para personalizar el diseño.
 * @param urgencies Lista de urgencias disponibles.
 * @param selectedUrgency Urgencia seleccionada.
 * @param onUrgencySelected Función para manejar la selección de una urgencia.
 * @see OutlinedTextField
 * @see Modifier
 * @see LocalDensity
 * */
@Composable
fun UrgencyFilter(
    urgencies: List<Urgency>,
    selectedUrgency: Urgency?,
    onUrgencySelected: (Urgency) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    val density = LocalDensity.current

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selectedUrgency?.name ?: "Selecciona",
            onValueChange = {},
            readOnly = true,
//            label = {
//                Text(
//                    "Urgencia",
//                    style = MaterialTheme.typography.labelMedium
//                )
//            },
            textStyle = MaterialTheme.typography.labelLarge,
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            },
            colors = OutlinedTextFieldDefaults.colors(),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 36.dp)
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                },
            singleLine = true,
            shape = MaterialTheme.shapes.small
        )

        Spacer(
            modifier = Modifier
                .matchParentSize()
                .clickable { expanded = !expanded }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(density) { textFieldSize.width.toDp() })
                .heightIn(max = 200.dp)
        ) {
            urgencies.forEach { urgency ->
                DropdownMenuItem(
                    onClick = {
                        onUrgencySelected(urgency)
                        expanded = false
                    },
                    text = {
                        Text(
                            urgency.name,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                )
            }
        }
    }
}
