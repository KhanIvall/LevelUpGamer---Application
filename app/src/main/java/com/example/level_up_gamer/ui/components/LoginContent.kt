package com.example.level_up_gamer.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.level_up_gamer.R

@Composable
fun LoginContent(
    navController: NavController,
    errorApi: String?,
    onLoginClick: (String, String) -> Unit
) {
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isErrorCorreo by remember { mutableStateOf(false) }
    var isErrorPassword by remember { mutableStateOf(false) }
    var showMessage by remember { mutableStateOf(false) }
    var messageType by remember { mutableStateOf(MessageType.ERROR) }
    var messageText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo_sin_fondo),
            contentDescription = "Logo Level Up Gamer",
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 16.dp)
        )

        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(32.dp))

        CampoTextoGamer(
            value = correo,
            onValueChange = {
                correo = it
                isErrorCorreo = false
            },
            label = "Usuario Gamer",
            isError = isErrorCorreo
        )
        if (isErrorCorreo) Text("Ingresa tu usuario", color = Color.Red)

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

        AnimatedVisibility(
            visible = showMessage,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            InfoMessage(
                message = messageText,
                type = messageType
            )
        }

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

        Spacer(modifier = Modifier.height(16.dp))

        GamerButton(
            text = "REGISTRARSE",
            onClick = {
                navController.navigate("registro")
            }
        )
    }
}