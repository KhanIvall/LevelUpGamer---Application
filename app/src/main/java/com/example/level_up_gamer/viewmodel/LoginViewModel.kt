package com.example.level_up_gamer.viewmodel
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// --- TUS IMPORTACIONES ---
// Asegúrate de que tus pantallas estén en el paquete ui.screens
import com.example.level_up_gamer.ui.screens.LoginScreen
import com.example.level_up_gamer.ui.screens.TiendaScreen
// Asegúrate de que tus ViewModels estén en el paquete viewmodel
import com.example.level_up_gamer.viewmodel.LoginViewModel
import com.example.level_up_gamer.viewmodel.TiendaViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // El NavHost gestiona el intercambio de pantallas
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        // ---------------------------------------------------------
        // RUTA 1: LOGIN
        // ---------------------------------------------------------
        composable("login") {
            // Creamos el ViewModel aquí. Gracias a Hilt o al Factory por defecto,
            // sobrevivirá a giros de pantalla.
            val loginViewModel: LoginViewModel = viewModel()

            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = { usuario ->
                    // Cuando el login es correcto, navegamos a la tienda.
                    // Pasamos el ID del usuario en la ruta.
                    navController.navigate("tienda/${usuario.id}") {
                        // "popUpTo" elimina la pantalla de login del historial
                        // para que al dar "Atrás" no vuelvas al login, sino que salgas de la app.
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // ---------------------------------------------------------
        // RUTA 2: TIENDA (Recibe el ID del usuario)
        // ---------------------------------------------------------
        composable(
            route = "tienda/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->

            // Recuperamos el argumento (ID del usuario) de la ruta
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0

            val tiendaViewModel: TiendaViewModel = viewModel()

            // Llamamos a la pantalla de la tienda
            TiendaScreen(
                viewModel = tiendaViewModel,
                usuarioId = userId,
                onLogout = {
                    // Si el usuario cierra sesión, volvemos al login
                    navController.navigate("login") {
                        popUpTo("tienda") { inclusive = true }
                    }
                }
            )
        }
    }
}