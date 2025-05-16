// MainActivity.kt
package com.project

//import com.project.data.repository.AuthRepository
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.project.data.repository.AuthRepository
import com.project.navigation.AppNavGraph
import com.project.ui.screens.gym.GymScreen
import com.project.ui.theme.MyAppTheme
import org.koin.android.ext.android.inject
import com.project.ui.theme.backgroundLight

class MainActivity : ComponentActivity() {
    // ...

    private val authRepository: AuthRepository by inject()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val startDestination = if (authRepository.isUserLoggedIn()) "home" else "login"
            MyAppTheme (darkTheme = false, dynamicColor = false){
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavGraph(
                        navController = navController,
                        startDestination = startDestination
                    )
                }
//                GymScreen(navController)
            }
        }

    }
}