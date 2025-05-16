package com.project.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AddToHomeScreen
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Input
import androidx.compose.material.icons.automirrored.filled.More
import androidx.compose.material.icons.automirrored.filled.NoteAdd
import androidx.compose.material.icons.automirrored.filled.Segment
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.ui.theme.MyAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    title: String = "TopAppBar",
    canNavigateBack: Boolean = true,
    onNavigateBack: () -> Unit = {}
) {
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        title = { Text(text = title) },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Navigate Back"
                    )
                }
            }
        }

    )

}

@Composable
fun AppBottomBar(
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ) {

    }
}


@Composable
fun FAB(
    onFirstClick: () -> Unit = {},
    onSecondClick: () -> Unit = {},
    onThirdClick: () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.clip(shape = MaterialTheme.shapes.medium),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.End
        ) {
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .clearAndSetSemantics {}
                        .background(Color.Transparent), // Fondo garantizado transparente
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    FloatingActionButton(onClick = { onFirstClick() }) {
                        Icon(Icons.Default.Book, contentDescription = "Opción 1")
                    }
                    FloatingActionButton(onClick = { onSecondClick() }) {
                        Icon(Icons.Default.Person, contentDescription = "Opción 2")
                    }
                    FloatingActionButton(onClick = { onThirdClick() }) {
                        Icon(Icons.Default.FitnessCenter, contentDescription = "Opción 3")
                    }
                }
            }

            FloatingActionButton(
                onClick = { expanded = !expanded },
                shape = FloatingActionButtonDefaults.shape,
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                contentColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = if (expanded) Icons.Default.Close else Icons.Default.Add,
                    contentDescription = "Abrir opciones"

                )
            }
        }
    }

}





@Preview
@Composable
fun FABPreview() {
    FAB()

}


@Preview
@Composable
fun AppTopBarPreview() {
    AppTopBar()

}