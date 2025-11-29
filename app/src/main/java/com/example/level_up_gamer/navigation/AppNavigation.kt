package com.example.level_up_gamer.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.level_up_gamer.ui.screen.LoginScreen
import com.example.level_up_gamer.ui.screen.RegistroScreen
import com.example.level_up_gamer.ui.screen.TiendaScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Definimos las rutas como constantes para evitar errores de escritura
    // Estructura: "nombre_pantalla"
    NavHost(navController = navController, startDestination = "login") {

        composable ("login") { LoginScreen(
            navController = navController,
            onLoginSuccess = { usuario ->
                navController.navigate("tienda/${usuario.id}") {
                    popUpTo("login") { inclusive = true }
                }
            })
        }

        composable("registro") { RegistroScreen(
            navController = navController,
            onSigninSuccess = { usuario ->
                navController.navigate("tienda/${usuario.id}") {
                    popUpTo("registro") { inclusive = true }
                }
            })
        }

        composable ("tienda/{userId}") { TiendaScreen(
            viewModel = viewModel(),
            usuarioId = it.arguments?.getString("userId")!!.toInt(),
            onLogout = {
                navController.navigate("login") {
                    popUpTo("tienda") { inclusive = true }
                }
            })
        }

        composable ("perfil/{userId}") {  }
    }
}