package com.project.ui.screens.study

// Importaciones necesarias
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.project.data.Task
import com.project.data.Urgency
import com.project.ui.component.*
import org.koin.androidx.compose.koinViewModel

/**
 * Pantalla de creación de tareas relacionadas con estudios.
 * Este Composable sirve como punto de entrada y se encarga de inicializar el ViewModel mediante Koin.
 *
 * @param navController Controlador de navegación de Jetpack Navigation.
 * @param viewModel ViewModel de la pantalla, inyectado por Koin.
 * @see StudyViewModel
 * @see NavController
 * @see Study
 * @see Scaffold
 * @see AppTopBar
 * @see TaskActionButtons
 * @see NotesInputField
 * @see UrgencyDropDown
 * @see DatePickerField
 * @see LocationInputField
 * @see TaskMap
 * @see AnnotationField
 * @see LaunchedEffect
 * @see remember
 * @see LocalContext
 * @see Places
 * @see MutableState
 * @see remember
 * @see mutableStateOf
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StudyScreen(
    navController: NavController,
    viewModel: StudyViewModel = koinViewModel()
) {
    Study(navController = navController, viewModel = viewModel)
}

/**
 * Contenido principal de la pantalla de estudios.
 * Permite al usuario ingresar información para crear una nueva tarea del tipo "Estudio".
 *
 * @param navController Controlador de navegación utilizado para regresar después de guardar la tarea.
 * @param viewModel ViewModel que maneja el estado y operaciones relacionadas con esta pantalla.
 * */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Study(navController: NavController, viewModel: StudyViewModel) {

    // Carga las urgencias disponibles al entrar en la pantalla
    LaunchedEffect(Unit) {
        viewModel.loadUrgencies()
    }

    // Contexto necesario para mostrar Toasts y crear el PlacesClient
    val context = LocalContext.current
    val placesClient = remember { Places.createClient(context) }

    // Estados locales para almacenar la información ingresada por el usuario
    var taskName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var annotation by remember { mutableStateOf("") }
    var placeQuery by remember { mutableStateOf("") }
    var selectedPlace by remember { mutableStateOf<Place?>(null) }
    var latitud by remember { mutableStateOf("") }
    var longitud by remember { mutableStateOf("") }
    var urgency by remember { mutableStateOf(Urgency()) }

    // Coordenadas del mapa que se mostrarán inicialmente (Madrid)
    var mapLocation by remember {
        mutableStateOf(LatLng(40.4168, -3.7038))
    }

    // Lista de niveles de urgencia desde el ViewModel
    val urgencyLevels by viewModel.urgencies.collectAsState()

    // Objeto Task que se construirá y enviará al ViewModel
    val task = Task(
        taskName = taskName,
        description = description,
        date = date,
        place = com.project.data.Place(lat = latitud, lon = longitud, name = placeQuery),
        annotation = annotation,
        categoryId = "Estudios", // Categoría fija
        urgency = urgency,
        tagIds = listOf() // Tags aún no utilizados
    )

    // Estructura visual principal
    Scaffold(
        topBar = {
            AppTopBar(
                title = "Nueva Tarea: Estudios",
                canNavigateBack = false,
                canLogOut = false
            )
        },
        bottomBar = {
            // Botones de acción para confirmar o cancelar
            TaskActionButtons(
                onCancel = { navController.popBackStack() },
                onConfirm = {
                    // Validación de campos requeridos
                    if (taskName.isBlank() || description.isBlank() || date.isBlank() || selectedPlace == null) {
                        Toast.makeText(
                            context, "Por favor, complete todos los campos.", Toast.LENGTH_LONG
                        ).show()
                    } else {
                        viewModel.insertTask(task = task)
                        navController.popBackStack() // Volver atrás después de guardar
                    }
                }
            )
        }
    ) { innerPadding ->
        // Contenedor principal de los campos de entrada
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Campo para nombre y descripción de la tarea
            NotesInputField(
                taskName = taskName,
                description = description,
                onTaskNameChange = { taskName = it },
                onDescriptionChange = { description = it }
            )

            // Fila con el selector de urgencia y fecha
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                UrgencyDropDown(
                    urgencyLevels = urgencyLevels,
                    selectedUrgency = urgency,
                    onUrgencyChange = { urgency = it },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                )

                DatePickerField(
                    selectedDate = date,
                    onDateSelected = { date = it },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                )
            }

            // Campo de búsqueda de lugar y selección con Google Places
            LocationInputField(
                query = placeQuery,
                onQueryChange = { placeQuery = it },
                placesClient = placesClient,
                onPlaceSelected = { place ->
                    selectedPlace = place
                    placeQuery = place.formattedAddress ?: ""
                    latitud = place.location?.latitude?.toString() ?: ""
                    longitud = place.location?.longitude?.toString() ?: ""
                    place.location?.let { mapLocation = it }
                },
                onLatitudChange = { latitud = it }
            )

            // Mapa que muestra la localización seleccionada
            TaskMap(location = mapLocation)

            // Campo para anotaciones adicionales
            AnnotationField(
                annotation = annotation,
                onAnnotationChange = { annotation = it }
            )
        }
    }
}

/**
 * Preview para la pantalla de estudios.
 * Requiere ajustes para funcionar debido a dependencias externas como ViewModel y NavController.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun GymPreview() {
    // Gym() ← Este preview está comentado porque no se ha definido un componente Gym
}
