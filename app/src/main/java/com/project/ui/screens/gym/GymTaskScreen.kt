package com.project.ui.screens.gym

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.project.ui.component.AnnotationField
import com.project.ui.component.AppTopBar
import com.project.ui.component.DatePickerField
import com.project.ui.component.LocationInputField
import com.project.ui.component.NotesInputField
import com.project.ui.component.TaskActionButtons
import com.project.ui.component.TaskMap
import com.project.ui.component.UrgencyDropDown
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GymScreen(
    navController: NavController, viewModel: GymViewModel = koinViewModel()
) {
    Gym(navController = navController, viewModel = viewModel)
}


//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun Gym(navController: NavController, viewModel: GymViewModel) {
//    val context = LocalContext.current
//    val placesClient = remember { Places.createClient(context) }
//
//    var taskName by remember { mutableStateOf("") }
//    var description by remember { mutableStateOf("") }
//    var date by remember { mutableStateOf("") }
//    var annotation by remember { mutableStateOf("") }
//    var placeQuery by remember { mutableStateOf("") }
//    var selectedPlace by remember { mutableStateOf<Place?>(null) }
//    var latitud by remember { mutableStateOf("") }
//    var longitud by remember { mutableStateOf("") }
//    var urgency by remember { mutableStateOf("") }
//    val task = Task(
//        taskName = taskName,
//        description = description,
//        date = date,
//        place = com.project.data.Place(lat = latitud, lon = longitud, name = placeQuery),
//        annotation = annotation,
//        categoryId = "Training",
//        urgencyId = urgency,
//        tagIds = listOf()
//    )
//    // Ubicación predeterminada: centro de Madrid
//    var mapLocation by remember {
//        mutableStateOf(LatLng(40.4168, -3.7038))  // Madrid
//    }

//    Scaffold(topBar = { GymTopBar() }, bottomBar = {
//        ConfirmCancelButtons(onAccept = {
//            if (taskName.isBlank() || description.isBlank() || date.isBlank() || selectedPlace == null) {
//                Toast.makeText(
//                    context, "Por favor, complete todos los campos.", Toast.LENGTH_LONG
//                ).show()
//            } else {
//                viewModel.insertTask(
//                    task = task
//                )
//                navController.popBackStack()
//            }
//        }, onCancel = { navController.popBackStack() })
//    }) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
////            InputFields(
////                taskName = taskName,
////                description = description,
////                date = date,
////                onTaskNameChange = { taskName = it },
////                onDescriptionChange = { description = it },
////                onDateChange = { date = it }
////            )
//
//            // Campo de autocompletado para lugar
//            SimplePlaceAutocompleteTextField(
//                query = placeQuery,
//                onQueryChange = { placeQuery = it },
//                placesClient = placesClient,
//                onPlaceSelected = { place ->
//                    selectedPlace = place
//                    placeQuery = place.formattedAddress ?: ""
//                    latitud = place.location?.latitude?.toString() ?: ""
//                    longitud = place.location?.longitude?.toString() ?: ""
//                    place.location?.let {
//                        mapLocation =
//                            it // Actualiza la ubicación del mapa con la ubicación seleccionada
//                    }
//                },
//                onLatitudChange = { latitud = it })
//
//            MyMap(location = mapLocation)  // Siempre muestra el mapa
//
//
////
//            AnnotationField(
//                annotation = annotation, onAnnotationChange = { annotation = it })
//        }
//    }
//}
//

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Gym(navController: NavController, viewModel: GymViewModel) {

    LaunchedEffect(Unit) {
        viewModel.loadUrgencies()
    }

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
    var urgency by remember { mutableStateOf(Urgency()) } // Inicializas con una urgencia por defecto


    var mapLocation by remember {
        mutableStateOf(LatLng(40.4168, -3.7038))  // Madrid
    }

    val urgencyLevels by viewModel.urgencies.collectAsState()
    val task = Task(

        taskName = taskName,
        description = description,
        date = date,
        place = com.project.data.Place(lat = latitud, lon = longitud, name = placeQuery),
        annotation = annotation,
        categoryId = "Entrenamiento",
        urgency = urgency,
        tagIds = listOf()
    )

    Scaffold(
        topBar = { AppTopBar(title = "Nueva Tarea: Deporte", canNavigateBack = false) },
        bottomBar = {
            TaskActionButtons(onCancel = { navController.popBackStack() }, onConfirm = {
                if (taskName.isBlank() || description.isBlank() || date.isBlank() || selectedPlace == null) {
                    Toast.makeText(
                        context, "Por favor, complete todos los campos.", Toast.LENGTH_LONG
                    ).show()
                } else {
                    viewModel.insertTask(
                        task = task
                    )
                    navController.popBackStack()
                }
            })
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            NotesInputField(
                taskName = taskName,
                description = description,
                onTaskNameChange = { taskName = it },
                onDescriptionChange = { description = it })
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
                    onUrgencyChange = { newUrgency ->
                        urgency = newUrgency
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp) // altura fija
                )

                DatePickerField(
                    selectedDate = date,
                    onDateSelected = { date = it },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp) // altura fija
                )
            }



            LocationInputField(
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
                onLatitudChange = { latitud = it })
            TaskMap(location = mapLocation)
            AnnotationField(annotation = annotation, onAnnotationChange = { annotation = it })
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun GymPreview() {
//    Gym()
}