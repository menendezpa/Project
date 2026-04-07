# Project — Gestor de Tareas Android

Aplicación Android de gestión de tareas personales desarrollada con **Kotlin** y **Jetpack Compose**. Permite organizar tareas por categorías (Estudio, Gimnasio, Social) con soporte para niveles de urgencia, recordatorios, ubicaciones en el mapa y sincronización en la nube mediante Firebase.

---

## Características

- **Autenticación**: Registro e inicio de sesión con Firebase Auth.
- **Gestión de tareas**: Crear, editar, eliminar y marcar tareas como completadas.
- **Categorías**: Estudio, Gimnasio y Social, cada una con su propia pantalla.
- **Urgencia**: Niveles de urgencia (Alta, Media, Baja) con indicador visual de color.
- **Calendario integrado**: Vista mensual con marcadores en los días que tienen tareas.
- **Filtros**: Filtrado de tareas por categoría y por nivel de urgencia.
- **Ubicación**: Asociar una ubicación a cada tarea mediante Google Maps / Places.
- **Anotaciones**: Campo adicional de notas por tarea.
- **Sincronización en la nube**: Datos persistidos en Firebase Firestore en tiempo real.

---

## Tecnologías

| Área | Librería / Servicio |
|---|---|
| Lenguaje | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Navegación | Navigation Compose |
| Inyección de dependencias | Koin |
| Backend / Auth | Firebase Auth + Firestore |
| Mapas | Google Maps SDK + Places SDK |
| Testing | JUnit · Espresso · Mockito |

---

## Estructura del proyecto

```
app/src/main/java/com/project/
├── data/               # Modelos de datos (Task, Category, Urgency, Place, Reminder…)
│   └── repository/     # Repositorios para Firestore (UserRepository, AuthRepository)
├── di/                 # Módulos de Koin
├── navigation/         # Grafo de navegación (NavGraph)
├── ui/
│   ├── component/      # Componentes reutilizables (Calendar, FloatingTask, Filters…)
│   ├── screens/        # Pantallas (Login, Register, Home, Gym, Social, Study)
│   └── theme/          # Tema, colores, tipografía y formas
├── MainActivity.kt
└── MyAplication.kt
```

---

## Pantallas

| Ruta | Pantalla |
|---|---|
| `login` | Inicio de sesión |
| `register` | Registro de usuario |
| `home` | Pantalla principal con calendario y lista de tareas |
| `StudyScreen` | Tareas de Estudio |
| `SocialScreen` | Tareas Sociales |
| `GymScreen` | Tareas de Gimnasio |

---

## Requisitos previos

- Android Studio Hedgehog (2023.1) o superior
- JDK 11
- Cuenta de Google con un proyecto Firebase configurado
- Clave de API de Google Maps

---

## Configuración

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/menendezpa/Project.git
   cd Project
   ```

2. **Firebase**
   - Crea un proyecto en [Firebase Console](https://console.firebase.google.com/).
   - Habilita **Authentication** (correo/contraseña) y **Firestore**.
   - Descarga el archivo `google-services.json` y colócalo en `app/`.

3. **Google Maps**
   - Obtén una clave de API en [Google Cloud Console](https://console.cloud.google.com/) con los productos **Maps SDK for Android** y **Places API** habilitados.
   - Añade la clave en `app/src/main/AndroidManifest.xml`:
     ```xml
     <meta-data
         android:name="com.google.android.geo.API_KEY"
         android:value="TU_API_KEY" />
     ```

4. **Compilar y ejecutar**
   - Abre el proyecto en Android Studio y pulsa **Run** (▶), o desde la terminal:
     ```bash
     ./gradlew assembleDebug
     ```

---

## Licencia

Distribuido bajo la licencia MIT. Consulta el archivo `LICENSE` para más información.
