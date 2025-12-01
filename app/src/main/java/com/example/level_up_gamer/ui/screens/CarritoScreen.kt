package com.example.level_up_gamer.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.level_up_gamer.model.DetallePedido
import com.example.level_up_gamer.model.Producto
import com.example.level_up_gamer.viewmodel.CarritoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    viewModel: CarritoViewModel = viewModel(),
    usuarioId: Int // 1. Recibimos el ID del usuario
) {

    val itemsDelCarrito by viewModel.itemsDelCarrito.collectAsState()
    val total by viewModel.total.collectAsState()

    // 2. Usamos LaunchedEffect para cargar los datos cuando la pantalla aparece
    LaunchedEffect(usuarioId) {
        viewModel.cargarCarritoDelUsuario(usuarioId)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mi Carrito") })
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            if (itemsDelCarrito.isEmpty()) {
                Text("Tu carrito está vacío.")
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(itemsDelCarrito) { (producto, detalle) ->
                        ItemCarrito(producto = producto, detalle = detalle)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Text("Total: ", fontSize = 20.sp)
                    Text(String.format("$%.2f", total), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

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