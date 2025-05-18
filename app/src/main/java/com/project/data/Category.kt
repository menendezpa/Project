package com.project.data

import com.google.firebase.firestore.DocumentId

data class Category(
    @set:DocumentId var id: String = "",
    val name: String = "Todas",
//    val colorHex: String = "#2196F3"
)
