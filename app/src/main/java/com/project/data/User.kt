package com.project.data

import com.google.firebase.firestore.DocumentId

data class User(
    @set:DocumentId var id: String = "",
    val name: String = "",
    val email: String = ""
)
