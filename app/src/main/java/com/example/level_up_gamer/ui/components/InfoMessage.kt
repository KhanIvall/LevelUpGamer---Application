package com.example.level_up_gamer.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class MessageType {
    ERROR,
    WARNING,
    SUCCESS
}

@Composable
fun InfoMessage(
    message: String,
    type: MessageType
) {
    val backgroundColor = when (type) {
        MessageType.ERROR -> Color(0xFFFFEBEE)
        MessageType.WARNING -> Color(0xFFFFF9C4)
        MessageType.SUCCESS -> Color(0xFFE8F5E9)
    }

    val textColor = when (type) {
        MessageType.ERROR -> Color(0xFFC62828)
        MessageType.WARNING -> Color(0xFFF17F17)
        MessageType.SUCCESS -> Color(0xFF2E7D32)
    }

    val icon = when (type) {
        MessageType.ERROR -> Icons.Default.Close
        MessageType.WARNING -> Icons.Default.Warning
        MessageType.SUCCESS -> Icons.Default.CheckCircle
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = message,
                color = textColor,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
@Preview()
fun InfoMessagePreview() {

    Column {
        InfoMessage(
            message = "Este es un mensaje",
            type = MessageType.SUCCESS
        )

        Spacer(modifier = Modifier.height(16.dp))

        InfoMessage(
            message = "Este es un mensaje",
            type = MessageType.WARNING
        )

        Spacer(modifier = Modifier.height(16.dp))

        InfoMessage(
            message = "Este es un mensaje",
            type = MessageType.ERROR
        )
    }
}