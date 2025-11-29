package com.example.level_up_gamer.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.level_up_gamer.model.Usuario
import com.example.level_up_gamer.ui.components.CampoTextoGamer
import com.example.level_up_gamer.ui.components.GamerButton
import com.example.level_up_gamer.ui.components.InfoMessage
import com.example.level_up_gamer.ui.components.LoginContent
import com.example.level_up_gamer.ui.components.MessageType
import com.example.level_up_gamer.viewmodel.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// 1. COMPONENTE CONECTADO (Tiene el ViewModel)
// Este es el que llama la Navegación
@Composable
fun LoginScreen(
    navController: NavController
    //onLoginSuccess: (Usuario) -> Unit
) {

    val viewModel: LoginViewModel = viewModel()

    val errorApi by viewModel.error.observeAsState()
    val usuarioLogueado by viewModel.usuarioLogueado.observeAsState()

/*
    // Efecto secundario de navegación
    LaunchedEffect(usuarioLogueado) {
        if (usuarioLogueado != null) {
            onLoginSuccess(usuarioLogueado!!)
        }
    }
*/
    // Llamamos al diseño puro y le pasamos solo lo que necesita
    LoginContent(
        navController = navController,
        errorApi = errorApi,
        onLoginClick = { correo, pass ->
            viewModel.login(correo, pass)
        }
    )
}