package com.project.ui.component

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.project.data.Urgency


@Composable
fun UrgencyDropDown(
    urgencyLevels: List<Urgency>,
    selectedUrgency: Urgency?,
    onUrgencyChange: (Urgency) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.height(56.dp)) { // Altura fija para mantener consistencia
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp) // Altura fija del botón
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f), // ocupa el espacio restante
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(
                                color = Color(
                                    (selectedUrgency?.colorHex ?: "#CCCCCC").toColorInt()
                                ),
                                shape = CircleShape
                            )
                    )
                    Text(
                        text = selectedUrgency?.name ?: "Seleccionar urgencia",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Expandir lista"
                )
            }
        }

        DropdownMenu(
            modifier = Modifier.height(190.dp),
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            urgencyLevels.forEach { urgency ->
                DropdownMenuItem(
                    onClick = {
                        onUrgencyChange(urgency)
                        expanded = false
                    },
                    text = {
                        UrgeIndicator(urgency)
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun UrgencyDropdownPreview() {
    val urgencies = listOf(
        Urgency("1", colorHex = "#E06C6A", name = "Alta"),
        Urgency("2", colorHex = "#EFE27F", name = "Media"),
        Urgency("3", colorHex = "#7FEF8F", name = "Baja")
    )

    var selectedUrgency by remember { mutableStateOf<Urgency?>(null) }

    UrgencyDropDown(
        urgencyLevels = urgencies,
        selectedUrgency = selectedUrgency,
        onUrgencyChange = { selectedUrgency = it }
    )
}

