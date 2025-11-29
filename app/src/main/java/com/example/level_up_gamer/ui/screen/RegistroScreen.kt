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

@Composable
fun RegistroScreen(
    navController: NavController,
    onSigninSuccess: (Usuario) -> Unit
) {
    val viewModel: LoginViewModel = viewModel()
    val errorApi by viewModel.error.observeAsState()

    val usuarioLogueado by viewModel.usuarioLogueado.observeAsState()

    LaunchedEffect(usuarioLogueado) {
        if (usuarioLogueado != null) {
            onSigninSuccess(usuarioLogueado!!)
        }
    }

    RegistroContent(
        navController = navController,
        errorApi = errorApi,
        onSigninClick = { usuario ->
            viewModel.registroUsuario(usuario)
        }
    )

}