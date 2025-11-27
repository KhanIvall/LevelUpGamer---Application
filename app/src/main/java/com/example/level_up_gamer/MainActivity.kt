package com.example.level_up_gamer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.level_up_gamer.navigation.AppNavigation
import com.example.level_up_gamer.ui.theme.LevelUpGamerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Esto permite que tu app use toda la pantalla (detrás de la barra de estado)
        enableEdgeToEdge()

        setContent {
            // 1. Aplicamos tu tema Gamer (Colores oscuros y Cyan)
            // Esto asegura que TODAS las pantallas dentro tengan el estilo correcto.
            LevelUpGamerTheme {

                // 2. Llamamos a tu sistema de navegación
                // Él decidirá si mostrar el Login o la Tienda.
                AppNavigation()
            }
        }
    }
}