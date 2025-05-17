package com.project.ui.screens.gym.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient


@Composable
fun LocationInputField(
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
                    isPlaceSelected =
                        false // Permite la edición nuevamente si el lugar fue seleccionado
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
                                    val address = place.formattedAddress ?: place.displayName ?: ""
                                    onQueryChange(address)  // Actualizamos el texto con la dirección del lugar
                                    onPlaceSelected(place)  // Pasa el lugar seleccionado
                                    onLatitudChange(place.location?.latitude?.toString() ?: "")
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

private fun fetchPlaceDetails(
    placeId: String,
    placesClient: PlacesClient,
    onPlaceDetailsFetched: (Place) -> Unit
) {
    val request = FetchPlaceRequest.builder(
        placeId,
        listOf(Place.Field.ID, Place.Field.DISPLAY_NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
    ).build()

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
