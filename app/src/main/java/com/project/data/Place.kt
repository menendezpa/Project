package com.project.data

import com.google.firebase.firestore.DocumentId

data class Place(
    @set:DocumentId var id: String = "",
    val lat: String = "",
    val lon: String = "",
    val name: String = ""
)
