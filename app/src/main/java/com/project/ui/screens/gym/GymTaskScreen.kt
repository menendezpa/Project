package com.project.ui.screens.gym

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.project.data.Task
import com.project.ui.component.*
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GymScreen(
    navController: NavController,
    viewModel: GymViewModel = koinViewModel()
) {
    Gym(navController = navController, viewModel = viewModel)
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Gym(navController: NavController, viewModel: GymViewModel) {
    val context = LocalContext.current
    val placesClient = remember { Places.createClient(context) }

    var taskName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var annotation by remember { mutableStateOf("") }
    var placeQuery by remember { mutableStateOf("") }
    var selectedPlace by remember { mutableStateOf<Place?>(null) }
    var latitud by remember { mutableStateOf("") }
    var longitud by remember { mutableStateOf("") }

    // Ubicación predeterminada: centro de Madrid
    var mapLocation by remember {
        mutableStateOf(com.google.android.gms.maps.model.LatLng(40.4168, -3.7038))  // Madrid
    }

    Scaffold(
        topBar = { GymTopBar() },
        bottomBar = {
            ConfirmCancelButtons(
                onAccept = {
                    if (taskName.isBlank() || description.isBlank() || date.isBlank() || selectedPlace == null) {
                        Toast.makeText(
                            context,
                            "Por favor, complete todos los campos.",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        viewModel.insertTask(
                            Task(
                                taskName = taskName,
                                description = description,
                                date = date,
                                place = com.project.data.Place(lat = latitud, lon = longitud, name = placeQuery )
                            )
                        )
                        navController.popBackStack()
                    }
                },
                onCancel = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InputFields(
                taskName = taskName,
                description = description,
                date = date,
                onTaskNameChange = { taskName = it },
                onDescriptionChange = { description = it },
                onDateChange = { date = it }
            )

            // Campo de autocompletado para lugar
            SimplePlaceAutocompleteTextField(
                query = placeQuery,
                onQueryChange = { placeQuery = it },
                placesClient = placesClient,
                onPlaceSelected = { place ->
                    selectedPlace = place
                    placeQuery = place.formattedAddress ?: ""
                    latitud = place.location?.latitude?.toString() ?: ""
                    longitud = place.location?.longitude?.toString() ?: ""
                    place.location?.let {
                        mapLocation =
                            it // Actualiza la ubicación del mapa con la ubicación seleccionada
                    }
                },
                onLatitudChange = { latitud = it }
            )

//            Text("Mapa del lugar:", style = MaterialTheme.typography.labelLarge)
            MyMap(location = mapLocation)  // Siempre muestra el mapa


//            selectedPlace?.location?.let { location ->
//                // Solo mostrar el mapa de la ubicación seleccionada si se ha seleccionado un lugar
//                Spacer(modifier = Modifier.height(12.dp))
//                Text("Ubicación en el mapa:", style = MaterialTheme.typography.labelLarge)
//                MyMap(location = location)
//            }

            AnnotationField(
                annotation = annotation,
                onAnnotationChange = { annotation = it }
            )
        }
    }
}