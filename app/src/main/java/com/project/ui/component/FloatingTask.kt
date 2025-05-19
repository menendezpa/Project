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
import androidx.compose.foundation.layout.height
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.project.data.Place
import com.project.data.Task
import com.project.data.Urgency


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FloatingTask(
    task: Task,
    onDismiss: () -> Unit,
    onSave: (Task) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    urgencyLevels: List<Urgency>
) {
    var taskName by remember { mutableStateOf(task.taskName) }
    var description by remember { mutableStateOf(task.description) }
    var urgency by remember { mutableStateOf(task.urgency) }
    var date by remember { mutableStateOf(task.date) }
    var annotation by remember { mutableStateOf(task.annotation) }
    var selectedPlace by remember {
        mutableStateOf(
            Place(
                lat = task.place.lat,
                lon = task.place.lon,
                name = task.place.name
            )
        )
    }

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })

    var latitud by remember { mutableStateOf(task.place.lat) }
    var longitud by remember { mutableStateOf(task.place.lon) }
    var mapLocation by remember {
        mutableStateOf(LatLng(task.place.lat.toDouble(), task.place.lon.toDouble()))
    }
    var state by remember { mutableStateOf(task.state) }

    // Query del lugar, inicializado con el lugar predeterminado
    var query by remember { mutableStateOf(task.place.name) }

    // Flag para controlar si el usuario está escribiendo o no
    var isUserTyping by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val placesClient = remember { Places.createClient(context) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    // Cuando cambie de página, reseteamos el flag para ocultar sugerencias si no está en página 1
    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != 1) {
            isUserTyping = false
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(24.dp)
                .widthIn(min = 320.dp, max = 600.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row {
                    Text(
                        text = "Editar Tarea",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    State(
                        currentState = state,
                        onClick = { newState -> state = newState }
                    )

                }

                NotesInputField(
                    taskName = taskName,
                    description = description,
                    onTaskNameChange = { taskName = it },
                    onDescriptionChange = { description = it }
                )

                Spacer(modifier = Modifier.height(4.dp))

                CustomPagerIndicator(currentPage = pagerState.currentPage, pageCount = 2)

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp) // un poco menos de altura para que no se vea gigante
                ) { page ->
                    when (page) {
                        0 -> {
                            Column {
                                Text(
                                    "Detalles",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )
                                FirstPage(
                                    annotation = annotation,
                                    onAnnotationChange = { annotation = it },
                                    urgencyLevels = urgencyLevels,
                                    selectedUrgency = urgency,
                                    onUrgencyChange = { urgency = it },
                                    selectedDate = date,
                                    onDateSelected = { date = it }
                                )
                            }
                        }

                        1 -> {
                            Column {
                                Text(
                                    "Ubicación",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )
                                SecondPage(
                                    query = query,
                                    onQueryChange = {
                                        query = it
                                        isUserTyping = true
                                    },
                                    placesClient = placesClient,
                                    onPlaceSelected = { place ->
                                        selectedPlace = Place(
                                            name = place.displayName ?: task.place.name,
                                            lat = place.location?.latitude?.toString()
                                                ?: task.place.lat,
                                            lon = place.location?.longitude?.toString()
                                                ?: task.place.lon
                                        )
                                        query = place.formattedAddress ?: ""
                                        latitud =
                                            place.location?.latitude?.toString() ?: task.place.lat
                                        longitud =
                                            place.location?.longitude?.toString() ?: task.place.lon
                                        place.location?.let {
                                            mapLocation = it
                                        }
                                        // Cuando se selecciona un lugar, dejamos de considerar que el usuario está escribiendo
                                        isUserTyping = false
                                    },
                                    onLatitudChange = { latitud = it },
                                    location = mapLocation,
                                    showSuggestions = isUserTyping && query.isNotBlank()  // Pasamos este flag para controlar la UI
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = { showDeleteConfirmation = true },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar tarea",
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    Row {
                        TextButton(onClick = onDismiss) {
                            Text("Cancelar")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = {
                                onSave(
                                    task.copy(
                                        taskName = taskName,
                                        description = description,
                                        urgency = urgency,
                                        date = date,
                                        annotation = annotation,
                                        place = selectedPlace,
                                        state = state,
                                    )
                                )
                            }
                        ) {
                            Text("Guardar")
                        }
                    }
                }
            }
        }
    }
    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Eliminar Tarea") },
            text = { Text("¿Estás seguro que quieres eliminar esta tarea?") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteConfirmation = false
                    onDelete() // Llamamos a la acción de borrar que viene por parámetro
                }) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text("Cancelar")
                }
            }
        )
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
    annotation: String,
    onAnnotationChange: (String) -> Unit,
    urgencyLevels: List<Urgency>,
    selectedUrgency: Urgency?,
    onUrgencyChange: (Urgency) -> Unit,
    selectedDate: String,
    onDateSelected: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
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
        AnnotationField(annotation = annotation, onAnnotationChange = onAnnotationChange)
    }
}


@Composable
fun SecondPage(
    query: String,
    onQueryChange: (String) -> Unit,
    placesClient: PlacesClient,
    onPlaceSelected: (com.google.android.libraries.places.api.model.Place) -> Unit,
    onLatitudChange: (String) -> Unit,
    location: LatLng,
    showSuggestions: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        LocationInputField(
            query = query,
            onQueryChange = onQueryChange,
            placesClient = placesClient,
            onPlaceSelected = onPlaceSelected,
            onLatitudChange = onLatitudChange,
            showSuggestions = showSuggestions
        )
        TaskMap(location = location)
    }

}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun FloatingTaskPreview2() {
    val task = Task(
        id = "1", taskName = "Task 1", description = "Description 1", date = "2025-05-01"
    )
//    FloatingTask(
//        task = task,
//        onDismiss = {},
//        onSave = {},
//        urgencyLevels = List(3) { Urgency(name = "Urgencia $it") })
}