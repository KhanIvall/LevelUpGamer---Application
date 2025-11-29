package com.example.level_up_gamer.ui.screen

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.level_up_gamer.model.Usuario
import com.example.level_up_gamer.ui.components.LoginContent
import com.example.level_up_gamer.viewmodel.LoginViewModel

// 1. COMPONENTE CONECTADO (Tiene el ViewModel)
// Este es el que llama la Navegación
@Composable
fun LoginScreen(
    navController: NavController,
    onLoginSuccess: (Usuario) -> Unit
) {

    val viewModel: LoginViewModel = viewModel()

    val errorApi by viewModel.error.observeAsState()
    val usuarioLogueado by viewModel.usuarioLogueado.observeAsState()

    // Efecto secundario de navegación
    LaunchedEffect(usuarioLogueado) {
        if (usuarioLogueado != null) {
            onLoginSuccess(usuarioLogueado!!)
        }
    }

    // Llamamos al diseño puro y le pasamos solo lo que necesita
    LoginContent(
        navController = navController,
        errorApi = errorApi,
        onLoginClick = { correo, pass ->
            viewModel.login(correo, pass)
        }
    )
}