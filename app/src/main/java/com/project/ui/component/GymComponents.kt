package com.project.ui.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.Place.Field
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

// Componente para los campos de entrada de tarea, descripción y fecha
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InputFields(
    taskName: String,
    description: String,
    date: String,
    onTaskNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onDateChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
//            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Row con Tarea y Fecha

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center

        ) {
            OutlinedTextField(
                value = taskName,
                onValueChange = { newValue -> onTaskNameChange(newValue) },  // Corregido para actualizar el estado
                modifier = Modifier.background(color = MaterialTheme.colorScheme.primaryContainer),
                placeholder = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) { Text("Nombre de Tarea", style = MaterialTheme.typography.titleLarge) }
                },
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                ),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
                singleLine = true,
            )
        }

        CustomDatePicker(
            initialDate = null,
            onDateSelected = { dateStr -> onDateChange(dateStr) },
            modifier = Modifier.fillMaxWidth()
        )


        // Campo de descripción
        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 4
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomDatePicker(
    modifier: Modifier = Modifier,
    initialDate: LocalDate? = null,
    onDateSelected: (String) -> Unit
) {
    var selectedDate by remember { mutableStateOf(initialDate) }
    var showDialog by remember { mutableStateOf(false) }
    var inputText by remember {
        mutableStateOf(
            initialDate?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) ?: ""
        )
    }

    // Formato de la fecha que se usará para la entrada
    val inputDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    // Formato de la fecha que se usará para almacenamiento (base de datos)
    val storageDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // Función para actualizar la fecha seleccionada
    fun updateDate(date: LocalDate?) {
        selectedDate = date
        inputText = date?.format(inputDateFormatter) ?: ""
        // Convertimos la fecha a formato ISO 'yyyy-MM-dd' para almacenarla en la base de datos
        onDateSelected(date?.format(storageDateFormatter) ?: "")
    }

    // Mostrar el cuadro de diálogo del selector de fecha
    if (showDialog) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    val millis = datePickerState.selectedDateMillis
                    val localDate = millis?.let { millisValue ->
                        Instant.ofEpochMilli(millisValue)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    }
                    updateDate(localDate)
                    showDialog = false
                }) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    OutlinedTextField(
        value = inputText,
        onValueChange = {
            inputText = it
            try {
                // Intentamos parsear la fecha en formato 'dd/MM/yyyy'
                val parsed = LocalDate.parse(it, inputDateFormatter)
                updateDate(parsed)
            } catch (e: DateTimeParseException) {
                // Si no se puede parsear la fecha, no realizamos ninguna acción
                onDateSelected("")
            }
        },
        label = { Text("Fecha") },
        modifier = modifier,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = "Seleccionar fecha",
                modifier = Modifier.clickable { showDialog = true }
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(
            onDone = { /* Acción al presionar 'done' */ }
        ),
        singleLine = true
    )
}


fun fetchPredictions(
    query: String,
    placesClient: PlacesClient,
    onResult: (List<AutocompletePrediction>) -> Unit
) {
    // Crea la solicitud de predicciones
    val request = FindAutocompletePredictionsRequest.builder()
        .setQuery(query)
        .setCountries("ES")  // Ajusta el país a tu preferencia
        .build()

    // Llama a la API para obtener las predicciones
    placesClient.findAutocompletePredictions(request)
        .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
            val predictions = response.autocompletePredictions
            onResult(predictions)
        }
        .addOnFailureListener { e ->
            e.printStackTrace()
            onResult(emptyList())
        }
}


@Composable
fun AutocompletePredictions(
    predictions: List<AutocompletePrediction>,
    onPredictionSelected: (AutocompletePrediction) -> Unit
) {
    LazyColumn {
        items(predictions) { prediction ->
            Text(
                text = prediction.getPrimaryText(null).toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onPredictionSelected(prediction)
                    },
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}


//@Composable
//fun SimplePlaceAutocompleteTextField(
//    query: String,
//    onQueryChange: (String) -> Unit,
//    placesClient: PlacesClient,
//    onPlaceSelected: (String) -> Unit
//) {
//    var predictions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }
//    var shouldIgnoreQueryChange by remember { mutableStateOf(false) }
//
//    // Solo consulta a la API si el texto cambia y no viene de una selección
//    LaunchedEffect(query) {
//        if (shouldIgnoreQueryChange) {
//            shouldIgnoreQueryChange = false
//            return@LaunchedEffect
//        }
//
//        if (query.isNotBlank()) {
//            val request = FindAutocompletePredictionsRequest.builder()
//                .setQuery(query)
//                .setCountries("ES") // Opcional: filtra por país
//                .build()
//
//            placesClient.findAutocompletePredictions(request)
//                .addOnSuccessListener { response ->
//                    predictions = response.autocompletePredictions
//                }
//                .addOnFailureListener {
//                    predictions = emptyList()
//                }
//        } else {
//            predictions = emptyList()
//        }
//    }
//
//    Column {
//        OutlinedTextField(
//            value = query,
//            onValueChange = onQueryChange,
//            label = { Text("Buscar lugar") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        if (predictions.isNotEmpty()) {
//            LazyColumn {
//                items(predictions) { prediction ->
//                    val primaryText = prediction.getPrimaryText(null).toString()
//                    Text(
//                        text = primaryText,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable {
//                                onPlaceSelected(primaryText)
//                                shouldIgnoreQueryChange = true
//                                onQueryChange(primaryText)
//                                predictions = emptyList()
//                            }
//                            .padding(8.dp)
//                    )
//                }
//            }
//        }
//    }
//}

@Composable
fun SimplePlaceAutocompleteTextField(
    query: String,
    onQueryChange: (String) -> Unit,
    placesClient: PlacesClient,
    onPlaceSelected: (Place) -> Unit,
    onLatitudChange: (String) -> Unit
) {
    // Estado para manejar las predicciones de lugares
    var predictions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }

    // Variable para determinar si hemos seleccionado un lugar
    var isPlaceSelected by remember { mutableStateOf(false) }

    // Ejecutar la búsqueda cuando el usuario escribe
    LaunchedEffect(query) {
        // Solo hacemos la búsqueda si no hemos seleccionado un lugar o si el texto está vacío
        if (query.isNotBlank() && !isPlaceSelected) {
            val request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .setCountries("ES")  // Limitar a España o la región que necesites
                .build()

            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    predictions = response.autocompletePredictions
                }
                .addOnFailureListener {
                    predictions = emptyList()
                }
        }
    }

    Column {
        // Componente de texto para escribir el lugar
        OutlinedTextField(
            value = query,
            onValueChange = {
                onQueryChange(it)
                if (isPlaceSelected) {
                    isPlaceSelected = false // Permite la edición nuevamente si el lugar fue seleccionado
                }
            },
            label = { Text("Buscar lugar") },
            modifier = Modifier.fillMaxWidth()
        )

        // Si hay predicciones y no se ha seleccionado un lugar, mostramos las sugerencias
        if (predictions.isNotEmpty() && query.isNotBlank() && !isPlaceSelected) {
            LazyColumn {
                items(predictions) { prediction ->
                    val primaryText = prediction.getPrimaryText(null).toString()
                    Text(
                        text = primaryText,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // Cuando se selecciona un lugar, lo almacenamos
                                isPlaceSelected = true
                                fetchPlaceDetails(prediction.placeId, placesClient) { place ->
                                    val address = place.formattedAddress ?: place.name ?: ""
                                    onQueryChange(address)  // Actualizamos el texto con la dirección del lugar
                                    onPlaceSelected(place)  // Pasa el lugar seleccionado
                                    onLatitudChange(place.latLng?.latitude?.toString() ?: "")
                                    predictions = emptyList() // Limpiamos las predicciones
                                }
                            }
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}

private fun fetchPlaceDetails(placeId: String, placesClient: PlacesClient, onPlaceDetailsFetched: (Place) -> Unit) {
    val request = FetchPlaceRequest.builder(placeId, listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)).build()

    placesClient.fetchPlace(request)
        .addOnSuccessListener { response ->
            val place = response.place
            onPlaceDetailsFetched(place)
        }
        .addOnFailureListener { exception ->
            // Manejo de errores
            println("Error al obtener los detalles del lugar: $exception")
        }
}


@Composable
fun ConfirmCancelButtons(
    onAccept: () -> Unit,
    onCancel: () -> Unit
) {
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
            onClick = onAccept,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Aceptar")
        }
    }
}



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


@Composable
fun MyMap(location: LatLng) {
    val cameraPositionState = rememberCameraPositionState()
    val markerState = rememberUpdatedMarkerState(position = location)

    LaunchedEffect(location) {
        cameraPositionState.animate(
            update = CameraUpdateFactory.newCameraPosition(
                CameraPosition.fromLatLngZoom(location, 15f)
            ),
            durationMs = 1000
        )
    }


    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = markerState,
            title = "Ubicación seleccionada"
        )
    }
}





@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun FullFormPreview() {
    var taskName by remember { mutableStateOf("Ir al gimnasio") }
    var description by remember { mutableStateOf("Entrenamiento de fuerza") }
    var date by remember { mutableStateOf("12/05/2025") }
    var locationQuery by remember { mutableStateOf("Madrid") }
    var annotation by remember { mutableStateOf("No olvidar la botella de agua") }

    // En esta preview no tenemos un PlacesClient real, así que usamos una lambda vacía
    Column(modifier = Modifier.padding(16.dp)) {
        InputFields(
            taskName = taskName,
            description = description,
            date = date,
            onTaskNameChange = { taskName = it },
            onDescriptionChange = { description = it },
            onDateChange = { date = it }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Solo muestra el TextField (sin predicciones porque no hay PlacesClient en preview)
        OutlinedTextField(
            value = locationQuery,
            onValueChange = { locationQuery = it },
            label = { Text("Lugar") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        AnnotationField(
            annotation = annotation,
            onAnnotationChange = { annotation = it }
        )

        ConfirmCancelButtons(
            onAccept = {},
            onCancel = {}
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GymTopBar(
) {
    TopAppBar(
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            scrolledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ) ,
        title = {
            Text(
                text = "Nueva Tarea: Deporte",
                style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onPrimary),
                modifier = Modifier.fillMaxWidth()
            )
        },
        actions = {
            // Aquí puedes agregar iconos o botones adicionales si es necesario
        }
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewInputFields() {
    var taskName by remember { mutableStateOf("Tarea Ejemplo") }
    var description by remember { mutableStateOf("Descripción de la tarea") }
    var date by remember { mutableStateOf("12/12/2025") }

    InputFields(
        taskName = taskName,
        description = description,
        date = date,
        onTaskNameChange = { taskName = it },
        onDescriptionChange = { description = it },
        onDateChange = { date = it }
    )
}
