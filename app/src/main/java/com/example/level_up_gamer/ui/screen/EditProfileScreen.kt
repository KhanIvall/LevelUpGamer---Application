package com.example.level_up_gamer.ui.screen

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.level_up_gamer.ui.components.EditProfileContent
import com.example.level_up_gamer.viewmodel.ProfileViewModel

@Composable
fun EditProfileScreen(
    navController: NavController,
    usuarioId: Int
) {
    val viewModel: ProfileViewModel = viewModel()
    val usuario by viewModel.usuario.observeAsState()
    val error by viewModel.error.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(initial = false)

    // Cargar datos del usuario al entrar a la pantalla
    LaunchedEffect(usuarioId) {
        viewModel.cargarUsuario(usuarioId)
    }

    // Navegar de vuelta al perfil después de actualizar
    LaunchedEffect(viewModel.updateSuccess.observeAsState().value) {
        if (viewModel.updateSuccess.value == true) {
            navController.popBackStack()
        }
    }

    EditProfileContent(
        navController = navController,
        usuario = usuario,
        error = error,
        isLoading = isLoading,
        onSaveClick = { nombre, correo, contrasenaActual, contrasenaNueva, fotoPerfil ->
            viewModel.actualizarPerfilCompleto(
                usuarioId = usuarioId,
                nuevoNombre = nombre,
                nuevoCorreo = correo,
                contrasenaActual = contrasenaActual,
                contrasenaNueva = contrasenaNueva,
                nuevaFotoPerfil = fotoPerfil
            )
        }
    )
}