package com.project.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.DocumentId
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
data class Task (
    @set:DocumentId var id: String = "",
    val userId: String = "",
    val taskName: String = "",
    val description: String = "",
    val state: String = "",             // Ej: "pendiente", "completada"
    val annotation: String = "",
    val place: Place = Place(),
    val date: String = LocalDate.now().toString(),
    val categoryId: String = "",        // ID de categoría
    val urgencyId: String = "",         // ID de urgencia
    val tagIds: List<String> = listOf() // IDs de etiquetas
) {
    val localDate: LocalDate?
        get() = try {
            LocalDate.parse(date)
        } catch (_: Exception) {
            null
        }
}


