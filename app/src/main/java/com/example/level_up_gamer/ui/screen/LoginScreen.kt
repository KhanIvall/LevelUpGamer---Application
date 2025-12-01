package com.example.level_up_gamer.ui.screen

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.level_up_gamer.model.Usuario
import com.example.level_up_gamer.ui.components.LoginContent
import com.example.level_up_gamer.viewmodel.LoginViewModel

/**
 * Composable con estado (stateful) que gestiona la lógica de la pantalla de login.
 * Se conecta al ViewModel, observa los cambios de estado y maneja los efectos secundarios como la navegación.
 */
@Composable
fun LoginScreen(
    navController: NavController,
    onLoginSuccess: (Usuario) -> Unit
) {
    val viewModel: LoginViewModel = viewModel()

    val errorApi by viewModel.error.observeAsState()
    val usuarioLogueado by viewModel.usuarioLogueado.observeAsState()

    // Efecto secundario que se dispara cuando el estado de `usuarioLogueado` cambia.
    // Si el login es exitoso, se ejecuta la lambda de navegación.
    LaunchedEffect(usuarioLogueado) {
        usuarioLogueado?.let { onLoginSuccess(it) }
    }

    // Se delega la renderización de la UI al composable sin estado.
    LoginContent(
        navController = navController,
        errorApi = errorApi,
        onLoginClick = { correo, pass ->
            viewModel.login(correo, pass)
        }
    )
}