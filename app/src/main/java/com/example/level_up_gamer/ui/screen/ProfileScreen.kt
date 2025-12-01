package com.example.level_up_gamer.ui.screen

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.level_up_gamer.ui.components.ProfileContent
import com.example.level_up_gamer.viewmodel.ProfileViewModel

/**
 * Composable con estado (stateful) para la pantalla de perfil del usuario.
 * Obtiene los datos del ProfileViewModel y gestiona la carga inicial de la información.
 */
@Composable
fun ProfileScreen(
    navController: NavController,
    usuarioId: Int,
    onLogout: () -> Unit
) {
    val viewModel: ProfileViewModel = viewModel()
    val usuario by viewModel.usuario.observeAsState()
    val error by viewModel.error.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(initial = false)

    // Efecto secundario para cargar los datos del usuario una sola vez cuando la pantalla se muestra.
    LaunchedEffect(usuarioId) {
        viewModel.cargarUsuario(usuarioId)
    }

    // Delega la renderización de la UI al composable sin estado ProfileContent.
    ProfileContent(
        navController = navController,
        usuario = usuario,
        error = error,
        isLoading = isLoading,
        onLogout = {
            // Antes de ejecutar la navegación de logout, se limpian los datos en el ViewModel.
            viewModel.limpiarDatos()
            onLogout()
        }
    )
}