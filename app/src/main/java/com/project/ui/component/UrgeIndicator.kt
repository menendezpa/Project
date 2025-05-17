package com.project.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.project.data.Urgency
import com.project.ui.component.darken

@Composable
fun UrgeIndicator(urgency: Urgency) {
    val backgroundColor = Color(urgency.colorHex.toColorInt())
    val circleColor = backgroundColor.darken(0.25f) // ahora sí se verá distinto

    Card(
        modifier = Modifier
            .width(120.dp)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Círculo con color más oscuro
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(circleColor)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = urgency.name,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
