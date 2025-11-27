package com.example.level_up_gamer.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Definimos SOLAMENTE el esquema oscuro (Gamer)
private val GamerColorScheme = darkColorScheme(
    primary = NeonCyan,          // Botones, Checkbox activos, etc.
    onPrimary = FondoDark,       // Color del texto DENTRO de un botón Cyan (para que se lea)
    secondary = NeonPurple,      // Botones flotantes o acentos
    background = FondoDark,      // El fondo de toda la pantalla
    surface = SurfaceDark,       // El fondo de las tarjetas (Items de juegos)
    onBackground = TextoBlanco,  // Texto sobre el fondo
    onSurface = TextoBlanco      // Texto sobre tarjetas
)

@Composable
fun LevelUpGamerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Esto detecta el tema del sistema, pero...
    content: @Composable () -> Unit
) {
    // ... aquí forzamos a que siempre use nuestros colores Gamer, ignorando el modo claro.
    val colorScheme = GamerColorScheme

    // Esto pinta la barra de estado (donde está la hora y batería) del color de tu fondo
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb() // Barra de estado oscura
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false // Iconos blancos
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Usa la tipografía por defecto (puedes cambiarla luego)
        content = content
    )
}