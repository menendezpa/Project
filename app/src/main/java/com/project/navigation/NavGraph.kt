// navigation/NavGraph.kt
package com.project.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.project.ui.screens.gym.GymScreen
import com.project.ui.screens.home.HomeScreen
import com.project.ui.screens.login.LoginScreen
import com.project.ui.screens.social.SocialScreen
import com.project.ui.screens.study.StudyScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph(navController: NavHostController, startDestination: String) {
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            composable("login") {
                LoginScreen(navController)
            }

            composable("register") {
                TODO(" RegisterScreen(navController)")
            }

            composable("home") {
                HomeScreen(navController)
            }
            composable("GymScreen") {
                GymScreen(navController)
            }

            composable("SocialScreen") {
                SocialScreen(navController)
            }
            composable("StudyScreen") {
                StudyScreen(navController)
            }



        }

}