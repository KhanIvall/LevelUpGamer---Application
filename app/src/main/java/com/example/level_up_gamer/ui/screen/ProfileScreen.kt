package com.example.level_up_gamer.ui.screen

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.level_up_gamer.ui.components.ProfileContent
import com.example.level_up_gamer.viewmodel.ProfileViewModel

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

    // Cargar datos del usuario al entrar a la pantalla
    LaunchedEffect(usuarioId) {
        viewModel.cargarUsuario(usuarioId)
    }

    ProfileContent(
        navController = navController,
        usuario = usuario,
        error = error,
        isLoading = isLoading,
        onLogout = {
            viewModel.limpiarDatos()
            onLogout()
        }
    )
}