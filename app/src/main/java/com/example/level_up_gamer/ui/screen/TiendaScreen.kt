package com.example.level_up_gamer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.level_up_gamer.model.Producto
import com.example.level_up_gamer.ui.components.ProductCard
import com.example.level_up_gamer.viewmodel.TiendaViewModel

// -------------------------------------------------------------------
// 1. PANTALLA CONECTADA (Lógica)
// -------------------------------------------------------------------
@Composable
fun TiendaScreen(
    viewModel: TiendaViewModel,
    usuarioId: Int,
    onLogout: () -> Unit
) {
    // 1. Observamos los datos del ViewModel
    val listaProductos by viewModel.productos.observeAsState(initial = emptyList())
    val totalCarrito by viewModel.totalCarrito.observeAsState(initial = 0.0)

    // 2. Cargamos los productos al entrar a la pantalla
    LaunchedEffect(Unit) {
        viewModel.cargarTodosLosProductos()
    }

    // 3. Llamamos al diseño puro
    TiendaContent(
        productos = listaProductos,
        totalCarrito = totalCarrito,
        onAgregar = { producto ->
            viewModel.agregarAlCarrito(producto)
        },
        onComprar = {
            viewModel.confirmarCompra(usuarioId)
        },
        onLogout = onLogout
    )
}

// -------------------------------------------------------------------
// 2. DISEÑO PURO (Visual)
// -------------------------------------------------------------------
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
                        // USAMOS TU COMPONENTE
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

// -------------------------------------------------------------------
// 3. PREVIEW (Para ver cómo queda sin ejecutar)
// -------------------------------------------------------------------
/*
@Preview
@Composable
fun TiendaPreview() {
    TiendaContent(
        productos = listOf(
            Producto(nombre = "Zelda TOTK", descripcion = "", precio = 59990.0, stock = 10, esVideojuego = true),
            Producto(nombre = "God of War", descripcion = "", precio = 64990.0, stock = 5, esVideojuego = true),
            Producto(nombre = "Catan", descripcion = "", precio = 35990.0, stock = 5, esVideojuego = false),
            Producto(nombre = "Dixit", descripcion = "", precio = 29990.0, stock = 2, esVideojuego = false)
        ),
        totalCarrito = 59990.0,
        onAgregar = {},
        onComprar = {},
        onLogout = {}
    )
}
*/