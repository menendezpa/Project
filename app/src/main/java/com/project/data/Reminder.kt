package com.project.data

import com.google.firebase.firestore.DocumentId

data class Reminder(
    @set:DocumentId var id: String = "",
    val taskId: String = "",
    val userId: String = "",
    val timestamp: String = "",     // Fecha y hora ISO o Firebase Timestamp
    val message: String = ""
)
