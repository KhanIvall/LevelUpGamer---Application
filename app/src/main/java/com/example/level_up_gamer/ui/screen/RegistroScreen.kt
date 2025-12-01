package com.example.level_up_gamer.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.level_up_gamer.model.Usuario
import com.example.level_up_gamer.ui.components.RegistroContent
import com.example.level_up_gamer.viewmodel.LoginViewModel

/**
 * Composable con estado (stateful) que gestiona la lógica de la pantalla de registro.
 * Se conecta al LoginViewModel para manejar la creación y autenticación automática del usuario.
 */
@Composable
fun RegistroScreen(
    navController: NavController,
    onSigninSuccess: (Usuario) -> Unit
) {
    val viewModel: LoginViewModel = viewModel()
    val errorApi by viewModel.error.observeAsState()

    val usuarioLogueado by viewModel.usuarioLogueado.observeAsState()

    // Efecto secundario que se dispara cuando el registro y login automático son exitosos.
    LaunchedEffect(usuarioLogueado) {
        usuarioLogueado?.let { onSigninSuccess(it) }
    }

    // Se delega la renderización de la UI al composable sin estado (stateless) RegistroContent.
    RegistroContent(
        navController = navController,
        errorApi = errorApi,
        onSigninClick = { usuario ->
            viewModel.registroUsuario(usuario)
        }
    )
}