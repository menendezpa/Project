package com.project.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.project.data.*
import kotlinx.coroutines.tasks.await

/**
 * Repositorio de datos relacionado con los usuarios, tareas y configuraciones auxiliares como urgencias,
 * categorías o ubicaciones.
 *
 * Este repositorio se comunica directamente con Firebase Firestore, y encapsula toda la lógica
 * para leer, escribir y actualizar los documentos de las colecciones relevantes.
 *
 * Colecciones utilizadas:
 * - USERS
 * - TASKS
 * - URGENCY
 * - PLACES
 * - TASKCATEGORIES
 */
class UserRepository {

    private val firestore = FirebaseFirestore.getInstance()

    private val userCollection = firestore.collection("USERS")
    private val taskCollection = firestore.collection("TASKS")
    private val placeCollection = firestore.collection("PLACES")
    private val urgencyCollection = firestore.collection("URGENCY")
    private val categoryCollection = firestore.collection("TASKCATEGORIES")

    /**
     * Crea un nuevo usuario en la colección USERS.
     *
     * @param user Objeto [User] que se desea registrar.
     */
    suspend fun createUser(user: User) {
        try {
            userCollection.add(user).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Obtiene un usuario de Firestore según su ID de documento.
     *
     * @param userId ID del documento de usuario.
     * @return Objeto [User] con los datos del usuario, o `null` si no se encontró.
     */
    suspend fun getUserById(userId: String): User? {
        return try {
            val documentSnapshot = userCollection.document(userId).get().await()
            documentSnapshot.toObject<User>()?.copy(id = documentSnapshot.id)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Recupera todas las tareas del usuario especificado.
     *
     * Nota: Actualmente la filtración por usuario se realiza en cliente, no en Firestore directamente.
     *
     * @param userId ID del usuario.
     * @return Lista de objetos [Task] asignadas al usuario.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getTasksForUser(userId: String): List<Task> {
        val tag = "getTasksForUser"
        return try {
            val snapshot = taskCollection.get().await()
            val filteredDocs = snapshot.documents.filter { doc ->
                doc.getString("userId") == userId
            }
            filteredDocs.mapNotNull { doc ->
                doc.toObject<Task>()?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            Log.e(tag, "Error en getTasksForUser", e)
            emptyList()
        }
    }

    /**
     * Obtiene la lista de niveles de urgencia disponibles en la colección URGENCY.
     *
     * @return Lista de objetos [Urgency].
     */
    suspend fun getUrgencies(): List<Urgency> {
        val tag = "getUrgencies"
        return try {
            val snapshot = urgencyCollection.get().await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject<Urgency>()?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            Log.e(tag, "Error en getUrgencies", e)
            emptyList()
        }
    }

    /**
     * Inserta una nueva tarea en Firestore y registra el lugar asociado si no existía previamente.
     *
     * @param userId ID del usuario que crea la tarea.
     * @param task Objeto [Task] que se desea registrar.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun insertTask(userId: String, task: Task) {
        try {
            val taskRef = taskCollection.add(task.copy(userId = userId)).await()

            // Verifica si el lugar ya está registrado antes de insertarlo
            if (task.place.name.isNotEmpty() && task.place.lat.isNotEmpty()) {
                val existingPlaces = placeCollection
                    .whereEqualTo("name", task.place.name)
                    .whereEqualTo("lat", task.place.lat)
                    .get()
                    .await()

                if (existingPlaces.isEmpty) {
                    val placeToInsert = hashMapOf(
                        "name" to task.place.name,
                        "lat" to task.place.lat,
                        "lon" to task.place.lon
                    )
                    placeCollection.add(placeToInsert).await()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Recupera todas las categorías de tareas disponibles.
     *
     * @return Lista de objetos [Category].
     */
    suspend fun getCategories(): List<Category> {
        val tag = "getCategories"
        return try {
            val snapshot = categoryCollection.get().await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject<Category>()?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            Log.e(tag, "Error en getCategories", e)
            emptyList()
        }
    }

    /**
     * Actualiza los datos de una tarea existente en la colección TASKS.
     *
     * @param task Objeto [Task] con el ID ya establecido.
     */
    suspend fun updateTask(task: Task) {
        try {
            taskCollection.document(task.id).set(task).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Elimina una tarea de Firestore.
     *
     * @param task Objeto [Task] a eliminar (requiere que el ID esté definido).
     */
    suspend fun deleteTask(task: Task) {
        try {
            taskCollection.document(task.id).delete().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
