package com.example.level_up_gamer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.level_up_gamer.model.Usuario
import com.example.level_up_gamer.ui.components.CampoTextoGamer
import com.example.level_up_gamer.ui.components.GamerButton
import com.example.level_up_gamer.viewmodel.LoginViewModel

// 1. COMPONENTE CONECTADO (Tiene el ViewModel)
// Este es el que llama la Navegación
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: (Usuario) -> Unit
) {
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
        errorApi = errorApi,
        onLoginClick = { correo, pass ->
            viewModel.login(correo, pass)
        }
    )
}

// 2. COMPONENTE DE DISEÑO PURO (Stateless)
// Este NO tiene ViewModel, solo recibe datos y eventos.
// ¡Este es el que podemos previsualizar!
@Composable
fun LoginContent(
    errorApi: String?,
    onLoginClick: (String, String) -> Unit
) {
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isErrorCorreo by remember { mutableStateOf(false) }
    var isErrorPassword by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "LEVEL UP",
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        CampoTextoGamer(
            value = correo,
            onValueChange = {
                correo = it
                isErrorCorreo = false
            },
            label = "Correo Gamer",
            isError = isErrorCorreo
        )
        if (isErrorCorreo) Text("Ingresa tu correo", color = Color.Red)

        Spacer(modifier = Modifier.height(16.dp))

        CampoTextoGamer(
            value = password,
            onValueChange = {
                password = it
                isErrorPassword = false
            },
            label = "Contraseña",
            visualTransformation = PasswordVisualTransformation(),
            isError = isErrorPassword
        )
        if (isErrorPassword) Text("Ingresa tu contraseña", color = Color.Red)

        if (errorApi != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorApi, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(24.dp))

        GamerButton(
            text = "INICIAR PARTIDA",
            onClick = {
                isErrorCorreo = correo.isEmpty()
                isErrorPassword = password.isEmpty()
                if (!isErrorCorreo && !isErrorPassword) {
                    onLoginClick(correo, password)
                }
            }
        )
    }
}

// 3. PREVIEW (Solo para ver en Android Studio)
/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    // Llamamos al Content, NO al Screen.
    // Pasamos datos falsos para ver cómo queda.
    LoginContent(
        errorApi = null, // Prueba poniendo "Error de conexión" aquí para ver cómo se ve
        onLoginClick = { _, _ -> } // Función vacía
    )
}
*/