package com.project.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Surface
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.project.data.Category
/**
 * Muestra un campo de texto para seleccionar una categoría.
 * @param modifier Modificador para personalizar el diseño.
 * @param categories Lista de categorías disponibles.
 * @param selectedCategory Categoría seleccionada.
 * @param onCategorySelected Función para manejar la selección de una categoría.
 * @see OutlinedTextField
 * @see Modifier
 * @see LocalDensity
 * */
@Composable
fun TaskFilter(
    categories: List<Category>,
    selectedCategory: Category?,
    onCategorySelected: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    val density = LocalDensity.current

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selectedCategory?.name ?: "Selecciona",
            onValueChange = {},
            readOnly = true,
//            label = {
//                Text(
//                    "Categoría",
//                    style = MaterialTheme.typography.labelSmall
//                )
//            },
            textStyle = MaterialTheme.typography.labelLarge, // Texto más pequeño
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp) // Ícono más pequeño
                )
            },
            colors = OutlinedTextFieldDefaults.colors(),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 36.dp) // Altura mínima más pequeña
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                },
            singleLine = true,
            // Este padding hace que el campo se vea más delgado
            shape = MaterialTheme.shapes.small
        )

        // Capa para clic
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
            categories.forEach { category ->
                DropdownMenuItem(
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    },
                    text = {
                        Text(
                            category.name,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun TaskFilterPreview() {
    val categories = List(20) { index -> Category(name = "Categoría ${index + 1}") }

    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    Surface(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TaskFilter(
            categories = categories,
            selectedCategory = selectedCategory,
            onCategorySelected = {
                println("Selected: ${it.name}")
                selectedCategory = it
            }
        )
    }
}
