package com.project.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.project.data.Task
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
            val snapshot = firestore.collection("TASKS")
                .get()
                .await()

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
                    Log.d(tag, "Documento ID: ${doc.id} descartado. userId en documento: '$docUserId'")
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

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getDaysWithTasks(userId: String): Set<LocalDate> {
        return try {
            // Realizamos una consulta filtrando en la colección "TASKS" por el userId
            val querySnapshot = taskCollection
                .whereEqualTo("userId", userId)
                .get()
                .await()

            // Mapeamos cada documento: extraemos el campo "date" (almacenado como String) y lo parseamos a LocalDate
            querySnapshot.documents.mapNotNull { document ->
                document.getString("date")?.let { dateStr ->
                    try {
                        LocalDate.parse(dateStr) // Se asume que la cadena está en formato ISO "yyyy-MM-dd"
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }
                }
            }.toSet()
        } catch (e: Exception) {
            e.printStackTrace()
            emptySet()
        }
    }


    suspend fun insertTask(userId: String, task: Task) {
        try {
            val taskToInsert = hashMapOf(
                "userId" to userId,
                "taskName" to task.taskName,
                "description" to task.description,
                "date" to task.date,
                "annotation" to task.annotation,
                "place" to task.place,
            )
            taskCollection.add(taskToInsert).await()

            if (task.place.name.isNotEmpty() && task.place.lat.isNotEmpty()) {
                val placeToInsert = hashMapOf(

                    "name" to task.place.name,
                    "address" to task.place.lat
                )
                firestore.collection("PLACES").add(placeToInsert).await()

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}
