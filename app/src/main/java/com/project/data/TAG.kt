package com.project.data

import com.google.firebase.firestore.DocumentId

data class TAG(
    @set:DocumentId var id: String = "",
    val name: String = ""  // Ej: "Urgente", "Personal"
)
