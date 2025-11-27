package com.example.level_up_gamer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.level_up_gamer.navigation.AppNavigation
import com.example.level_up_gamer.ui.theme.LevelUpGamerTheme // Importa tu tema

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Habilita pantalla completa (sin bordes negros arriba)
        setContent {
            // Envuelve tu navegación en el tema de la app
            LevelUpGamerTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun PantallaBienvenida(){
    var mensajeVisible by remember { mutableStateOf(false) }

    val colorFondo by animateColorAsState(
        targetValue = if(mensajeVisible)
            Color(0xFFBA68C8)
        else Color(0xFFBA68C8)

    )
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(colorFondo)
            .padding(30.dp)

    )
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LevelupgamerTheme {
        Greeting("Android")
    }
}