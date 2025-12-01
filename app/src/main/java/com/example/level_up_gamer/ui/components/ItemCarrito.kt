package com.example.level_up_gamer.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.level_up_gamer.model.DetallePedido
import com.example.level_up_gamer.model.Producto

@Composable
fun ItemCarrito(producto: Producto, detalle: DetallePedido) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.weight(1f)) {
                Text(producto.nombre, fontWeight = FontWeight.Bold)
                Text("Cantidad: ${detalle.cantidad}")
            }
            Text(String.format("$%.2f", detalle.precioUnitario * detalle.cantidad))
        }
    }
}