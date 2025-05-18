package com.project.data

import com.google.firebase.firestore.DocumentId

data class Urgency(
    @set:DocumentId var id: String = "",
    val name: String = "Urgencia",         // Ej: "Alta", "Media", "Baja"
    val level: Int = 1,            // 1 = Baja, 2 = Media, 3 = Alta
    val colorHex: String = "#c8bcba"
)
