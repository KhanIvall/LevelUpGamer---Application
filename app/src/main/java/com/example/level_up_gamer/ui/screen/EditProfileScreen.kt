package com.example.level_up_gamer.ui.screen

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.level_up_gamer.ui.components.EditProfileContent
import com.example.level_up_gamer.viewmodel.ProfileViewModel

/**
 * Composable con estado (stateful) para la pantalla de edición de perfil.
 * Gestiona la carga inicial de los datos del usuario y el efecto secundario de navegación
 * tras una actualización exitosa.
 */
@Composable
fun EditProfileScreen(
    navController: NavController,
    usuarioId: Int
) {
    val viewModel: ProfileViewModel = viewModel()
    val usuario by viewModel.usuario.observeAsState()
    val error by viewModel.error.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(initial = false)

    // Carga los datos del usuario una sola vez cuando la pantalla se muestra.
    LaunchedEffect(usuarioId) {
        viewModel.cargarUsuario(usuarioId)
    }

    // Efecto secundario que observa si la actualización fue exitosa para navegar hacia atrás.
    LaunchedEffect(viewModel.updateSuccess.observeAsState().value) {
        if (viewModel.updateSuccess.value == true) {
            navController.popBackStack()
        }
    }

    // Delega la renderización de la UI al composable sin estado EditProfileContent.
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