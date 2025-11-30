package com.example.level_up_gamer.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.level_up_gamer.R
import com.example.level_up_gamer.model.Usuario
import com.example.level_up_gamer.utils.ImagePickerHelper
import com.example.level_up_gamer.utils.rememberImagePickerLauncher
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileContent(
    navController: NavController,
    usuario: Usuario?,
    error: String?,
    isLoading: Boolean,
    onSaveClick: (String, String, String, String?, String?) -> Unit
) {
    val context = LocalContext.current
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasenaActual by remember { mutableStateOf("") }
    var contrasenaNueva by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imagePath by remember { mutableStateOf<String?>(null) }

    var isErrorNombre by remember { mutableStateOf(false) }
    var isErrorCorreo by remember { mutableStateOf(false) }
    var isErrorContrasenaActual by remember { mutableStateOf(false) }
    var isErrorContrasenaNueva by remember { mutableStateOf(false) }
    var passwordsNoCoinciden by remember { mutableStateOf(false) }

    // Cargar datos del usuario cuando estén disponibles
    LaunchedEffect(usuario) {
        usuario?.let {
            nombre = it.nombre
            correo = it.correo
            imagePath = it.fotoPerfil
        }
    }

    // Launcher para seleccionar imagen
    val imagePickerLauncher = rememberImagePickerLauncher { uri ->
        imageUri = uri
        // Guardar la imagen en almacenamiento interno
        usuario?.let {
            val savedPath = ImagePickerHelper.saveImageToInternalStorage(
                context = context,
                sourceUri = uri,
                userId = it.id
            )
            imagePath = savedPath
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "EDITAR PERFIL",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Avatar con opción de editar
            Box(
                modifier = Modifier.size(100.dp),
                contentAlignment = Alignment.Center
            ) {
                // Imagen de perfil
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        .clickable { imagePickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (imagePath != null && File(imagePath!!).exists()) {
                        // Mostrar imagen guardada
                        AsyncImage(
                            model = File(imagePath!!),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // Mostrar logo por defecto
                        Image(
                            painter = painterResource(id = R.drawable.logo_sin_fondo),
                            contentDescription = "Avatar",
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }

                // Icono de lápiz (editar) en la esquina
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { imagePickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = "Cambiar foto",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Toca para cambiar foto",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Formulario
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Información Personal",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo Nombre
                    CampoTextoGamer(
                        value = nombre,
                        onValueChange = {
                            nombre = it
                            isErrorNombre = false
                        },
                        label = "Nombre Gamer",
                        isError = isErrorNombre
                    )
                    if (isErrorNombre) Text("Ingresa tu nombre", color = Color.Red)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo Correo
                    CampoTextoGamer(
                        value = correo,
                        onValueChange = {
                            correo = it
                            isErrorCorreo = false
                        },
                        label = "Correo",
                        isError = isErrorCorreo
                    )
                    if (isErrorCorreo) Text("Ingresa tu correo", color = Color.Red)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Card para cambiar contraseña
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Cambiar Contraseña (Opcional)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Contraseña Actual
                    CampoTextoGamer(
                        value = contrasenaActual,
                        onValueChange = {
                            contrasenaActual = it
                            isErrorContrasenaActual = false
                        },
                        label = "Contraseña Actual",
                        visualTransformation = PasswordVisualTransformation(),
                        isError = isErrorContrasenaActual
                    )
                    if (isErrorContrasenaActual) Text("Ingresa tu contraseña actual", color = Color.Red)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Nueva Contraseña
                    CampoTextoGamer(
                        value = contrasenaNueva,
                        onValueChange = {
                            contrasenaNueva = it
                            isErrorContrasenaNueva = false
                            passwordsNoCoinciden = false
                        },
                        label = "Nueva Contraseña",
                        visualTransformation = PasswordVisualTransformation(),
                        isError = isErrorContrasenaNueva
                    )
                    if (isErrorContrasenaNueva) Text("Ingresa la nueva contraseña", color = Color.Red)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Confirmar Contraseña
                    CampoTextoGamer(
                        value = confirmarContrasena,
                        onValueChange = {
                            confirmarContrasena = it
                            passwordsNoCoinciden = false
                        },
                        label = "Confirmar Nueva Contraseña",
                        visualTransformation = PasswordVisualTransformation(),
                        isError = passwordsNoCoinciden
                    )
                    if (passwordsNoCoinciden) Text("Las contraseñas no coinciden", color = Color.Red)
                }
            }

            if (error != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = error,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Guardar
            GamerButton(
                text = if (isLoading) "GUARDANDO..." else "GUARDAR CAMBIOS",
                onClick = {
                    // Validaciones
                    isErrorNombre = nombre.isEmpty()
                    isErrorCorreo = correo.isEmpty()

                    // Si quiere cambiar contraseña, validar campos
                    val quiereCambiarContrasena = contrasenaActual.isNotEmpty() ||
                            contrasenaNueva.isNotEmpty() ||
                            confirmarContrasena.isNotEmpty()

                    if (quiereCambiarContrasena) {
                        isErrorContrasenaActual = contrasenaActual.isEmpty()
                        isErrorContrasenaNueva = contrasenaNueva.isEmpty()
                        passwordsNoCoinciden = contrasenaNueva != confirmarContrasena &&
                                contrasenaNueva.isNotEmpty() &&
                                confirmarContrasena.isNotEmpty()
                    }

                    // Si todo está bien, guardar
                    if (!isErrorNombre && !isErrorCorreo && !passwordsNoCoinciden) {
                        if (quiereCambiarContrasena && !isErrorContrasenaActual && !isErrorContrasenaNueva) {
                            onSaveClick(nombre, correo, contrasenaActual, contrasenaNueva, imagePath)
                        } else if (!quiereCambiarContrasena) {
                            onSaveClick(nombre, correo, "", null, imagePath)
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}