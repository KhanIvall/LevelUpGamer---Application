package com.example.level_up_gamer.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.level_up_gamer.ui.screen.LoginScreen
import com.example.level_up_gamer.ui.screen.TiendaScreen
import com.example.level_up_gamer.viewmodel.LoginViewModel
import com.example.level_up_gamer.viewmodel.TiendaViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Definimos las rutas como constantes para evitar errores de escritura
    // Estructura: "nombre_pantalla"
    NavHost(navController = navController, startDestination = "login") {

        // --- PANTALLA 1: LOGIN ---
        composable("login") {
            // Instanciamos el ViewModel aquí o dentro de la pantalla
            val loginViewModel: LoginViewModel = viewModel()

            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = { usuario ->
                    // Navegamos a la tienda pasando el ID del usuario
                    navController.navigate("tienda/${usuario.id}") {
                        // Esto borra el login de la pila "atrás" para que no vuelvan al login
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // --- PANTALLA 2: TIENDA ---
        // Definimos que esta ruta recibe un argumento: userId
        composable(
            route = "tienda/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            // Recuperamos el ID del usuario que viene del Login
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0

            val tiendaViewModel: TiendaViewModel = viewModel()

            TiendaScreen(
                viewModel = tiendaViewModel,
                usuarioId = userId,
                onLogout = {
                    // Si implementas un botón de salir, vuelves al login
                    navController.navigate("login") {
                        popUpTo("tienda") { inclusive = true }
                    }
                }
            )
        }
    }
}