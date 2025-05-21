package com.project.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
/**
 * Muestra un campo de texto para ingresar una ubicación.
 * @param location Valor de la ubicación.
 * */
@Composable
fun TaskMap(location: LatLng) {
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


@Preview
@Composable
fun TaskMapPreview() {
    TaskMap(location =  LatLng(40.4167754, -3.7037902))
}