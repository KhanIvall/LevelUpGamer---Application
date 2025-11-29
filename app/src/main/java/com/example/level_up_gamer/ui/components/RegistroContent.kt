package com.example.level_up_gamer.ui.components

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.level_up_gamer.R
import com.example.level_up_gamer.model.Usuario
import com.example.level_up_gamer.viewmodel.LoginViewModel

@Composable
fun RegistroContent(
    navController: NavController,
    errorApi: String?,
    onSigninClick: (Usuario) -> Unit
)
{
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var contrasenaRepetir by remember { mutableStateOf("") }
    var isErrorNombre by remember { mutableStateOf(false) }
    var isErrorCorreo by remember { mutableStateOf(false) }
    var isErrorPassword by remember { mutableStateOf(false) }
    var isErrorRepeatPassword by remember { mutableStateOf(false) }
    var passwordsNoCoinciden by remember { mutableStateOf(false) }

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
                .size(150.dp)
                .padding(bottom = 16.dp)
        )

        Text(
            text = "Registrarse",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(32.dp))

        CampoTextoGamer(
            value = nombre,
            onValueChange = {
                nombre = it
                isErrorNombre = false
            },
            label = "Nombre Gamer",
            isError = isErrorNombre
        )
        if (isErrorNombre) Text("Ingresa tu usuario", color = Color.Red)

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

        Spacer(modifier = Modifier.height(32.dp))

        CampoTextoGamer(
            value = contrasena,
            onValueChange = {
                contrasena = it
                isErrorPassword = false
            },
            label = "Constraseña Gamer",
            isError = isErrorPassword
        )
        if (isErrorPassword) Text("Ingresa tu constraseña", color = Color.Red)

        Spacer(modifier = Modifier.height(32.dp))

        CampoTextoGamer(
            value = contrasenaRepetir,
            onValueChange = {
                contrasenaRepetir = it
                isErrorRepeatPassword = false
            },
            label = "Repita Constraseña",
            isError = isErrorRepeatPassword
        )
        if (isErrorRepeatPassword) Text("Ingresa de nuevo tu contraseña", color = Color.Red)
        if (passwordsNoCoinciden) Text("Las contraseñas no coinciden", color = Color.Red)

        if (errorApi != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorApi, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(24.dp))

        GamerButton(
            text = "CREAR CUENTA",
            onClick = {
                isErrorNombre = nombre.isEmpty()
                isErrorCorreo = correo.isEmpty()
                isErrorPassword = contrasena.isEmpty()
                isErrorRepeatPassword = contrasena.isEmpty()
                passwordsNoCoinciden = contrasena != contrasenaRepetir && contrasena.isNotEmpty() && contrasenaRepetir.isNotEmpty()

                if (!isErrorNombre && !isErrorCorreo && !isErrorPassword && !isErrorRepeatPassword && contrasena == contrasenaRepetir) {
                    onSigninClick(Usuario(nombre = nombre, correo = correo, contrasena = contrasena))
                }
            }
        )
    }
}