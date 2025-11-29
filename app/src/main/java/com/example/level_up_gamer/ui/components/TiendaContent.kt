package com.example.level_up_gamer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.level_up_gamer.model.Producto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TiendaContent(
    productos: List<Producto>,
    totalCarrito: Double,
    onAgregar: (Producto) -> Unit,
    onComprar: () -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        // --- BARRA SUPERIOR ---
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "TIENDA LEVEL UP",
                        color = MaterialTheme.colorScheme.primary, // Cyan
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    // Botón para salir
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar Sesión",
                            tint = Color.Red
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background // Fondo oscuro
                )
            )
        },
        // --- BARRA INFERIOR (TOTAL Y PAGAR) ---
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Texto del total
                    Column {
                        Text("Total a Pagar:", style = MaterialTheme.typography.bodySmall)
                        Text(
                            text = "$ ${totalCarrito.toInt()}",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Botón de Pagar
                    Button(
                        onClick = onComprar,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary // Morado
                        ),
                        // Solo se habilita si hay algo en el carrito (total > 0)
                        enabled = totalCarrito > 0
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("PAGAR")
                    }
                }
            }
        }
    ) { paddingValues ->

        // --- CONTENIDO PRINCIPAL (La Grilla) ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Respetamos el espacio de las barras
                .background(MaterialTheme.colorScheme.background)
        ) {

            if (productos.isEmpty()) {
                // Mensaje si no hay datos
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Cargando inventario...", color = Color.Gray)
                }
            } else {
                // La Cuadrícula de productos (2 columnas)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(productos) { producto ->
                        ProductCard(
                            producto = producto,
                            onAgregarClick = { onAgregar(producto) }
                        )
                    }
                }
            }
        }
    }
}
