package com.example.level_up_gamer.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Botón de acción principal reutilizable con el estilo visual de la aplicación "LevelUp".
 *
 * @param text El texto que se mostrará en el botón (se convertirá a mayúsculas).
 * @param onClick La acción lambda que se ejecutará cuando el botón sea presionado.
 * @param modifier Modificador de Compose para personalizar el layout del botón.
 * @param enabled Controla si el botón está activo y puede ser presionado.
 */
@Composable
fun GamerButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        enabled = enabled
    ) {
        Text(
            text = text.uppercase(),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}