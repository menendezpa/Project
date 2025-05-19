package com.project.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.project.data.Category
import com.project.data.Place
import com.project.data.Task
import com.project.data.Urgency
import com.project.data.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import net.bytebuddy.asm.Advice.Local
import java.time.LocalDate

class UserRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val userCollection = firestore.collection("USERS")
    private val taskCollection = firestore.collection("TASKS")
    private val placeCollection = firestore.collection("PLACES")
    private val urgencyCollection = firestore.collection("URGENCY")
    private val categoryCollection = firestore.collection("TASKCATEGORIES")


    suspend fun createUser(user: User) {
        try {
            userCollection.add(user).await()
        } catch (e: Exception) {
            // Manejar errores, por ejemplo, loggearlos o lanzar una excepción personalizada
            e.printStackTrace()
        }
    }
    // Puedes añadir otras funciones para obtener un usuario específico, añadir uno nuevo, etc.
    suspend fun getUserById(userId: String): User? {
        return try {
            val documentSnapshot = userCollection.document(userId).get().await()
            documentSnapshot.toObject<User>()?.copy(id = documentSnapshot.id)
        } catch (e: Exception) {
            // Manejar errores, por ejemplo, loggearlos o lanzar una excepción personalizada
            e.printStackTrace()
            null
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getTasksForUser(userId: String): List<Task> {
        val firestore = FirebaseFirestore.getInstance()
        val tag = "getTasksForUser"
        return try {
            Log.d(tag, "Consultando todos los documentos de la colección TASKS")
            val snapshot = firestore.collection("TASKS").get().await()

            Log.d(tag, "Documentos totales en TASKS: ${snapshot.documents.size}")
            snapshot.documents.forEach { document ->
                Log.d(tag, "Documento ID: ${document.id} -> Datos: ${document.data}")
            }

            // Filtrar manualmente en el cliente por userId
            val filteredDocs = snapshot.documents.filter { doc ->
                val docUserId = doc.getString("userId") ?: ""
                if (docUserId == userId) {
                    Log.d(tag, "Documento ID: ${doc.id} incluido. userId: '$docUserId'")
                    true
                } else {
                    Log.d(
                        tag, "Documento ID: ${doc.id} descartado. userId en documento: '$docUserId'"
                    )
                    false
                }
            }
            Log.d(tag, "Documentos filtrados: ${filteredDocs.size}")

            val tasks = filteredDocs.mapNotNull { document ->
                val task = document.toObject<Task>()
                if (task == null) {
                    Log.d(tag, "No se pudo mapear el documento con ID: ${document.id}")
                }
                task?.copy(id = document.id)
            }
            Log.d(tag, "Tareas mapeadas: ${tasks.size}")
            tasks
        } catch (e: Exception) {
            Log.e(tag, "Error en getTasksForUser", e)
            emptyList()
        }
    }

    suspend fun getUrgencies(): List<Urgency> {
        val tag = "getUrgencies"
        return try {
            Log.d(tag, "Consultando todos los documentos de la colección URGENCY")
            val snapshot = urgencyCollection.get().await()
            Log.d(tag, "Documentos totales en URGENCY: ${snapshot.documents.size}")
            snapshot.documents.forEach { document ->
                Log.d(tag, "Documento ID: ${document.id} -> Datos: ${document.data}")
            }
            snapshot.documents.mapNotNull { document ->
                val urgency = document.toObject<Urgency>()
                if (urgency == null) {
                    Log.d(tag, "No se pudo mapear el documento con ID: ${document.id}")
                }
                urgency?.copy(id = document.id)
            }
        } catch (e: Exception) {
            Log.e(tag, "Error en getUrgencies", e)
            emptyList()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun insertTask(userId: String, task: Task) {
        try {


            // Insertar la tarea
            val taskRef = taskCollection.add(task.copy(userId = userId)).await()

            // Comprobamos si el lugar ya existe (por nombre y lat)
            if (task.place.name.isNotEmpty() && task.place.lat.isNotEmpty()) {
                val existingPlaces =
                    firestore.collection("PLACES").whereEqualTo("name", task.place.name)
                        .whereEqualTo("lat", task.place.lat).get().await()

                // Si no existe, lo insertamos
                if (existingPlaces.isEmpty) {
                    val placeToInsert = hashMapOf(
                        "name" to task.place.name, "lat" to task.place.lat, "lon" to task.place.lon
                    )

                    firestore.collection("PLACES").add(placeToInsert).await()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getCategories(): List<Category> {
        val tag = "getCategories"
        return try {
            Log.d(tag, "Consultando todos los documentos de la colección TASKCATEGORIES")
            val snapshot = categoryCollection.get().await()

            Log.d(tag, "Documentos totales en TASKCATEGORIES: ${snapshot.documents.size}")
            snapshot.documents.forEach { document ->
                Log.d(tag, "Documento ID: ${document.id} -> Datos: ${document.data}")
            }
            snapshot.documents.mapNotNull { document ->
                val category = document.toObject<Category>()
                if (category == null) {
                    Log.d(tag, "No se pudo mapear el documento con ID: ${document.id}")
                }
                category?.copy(id = document.id)
            }
        } catch (e: Exception) {
            Log.e(tag, "Error en getCategories", e)
            emptyList()
        }
    }

    suspend fun updateTask(task: Task) {
        try {
            taskCollection.document(task.id).set(task).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteTask(task: Task) {
        try {
            taskCollection.document(task.id).delete().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


