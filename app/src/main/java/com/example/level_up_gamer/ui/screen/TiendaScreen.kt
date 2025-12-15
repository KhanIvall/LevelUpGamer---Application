package com.example.level_up_gamer.ui.screen

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.level_up_gamer.ui.components.TiendaContent
import com.example.level_up_gamer.viewmodel.CarritoViewModel
import com.example.level_up_gamer.viewmodel.TiendaViewModel

/**
 * Composable con estado (stateful) para la pantalla principal de la tienda.
 * Orquesta la obtención de datos de múltiples ViewModels (catálogo y carrito)
 * y gestiona las acciones de navegación y de adición de productos.
 */
@Composable
fun TiendaScreen(
    tiendaViewModel: TiendaViewModel, 
    carritoViewModel: CarritoViewModel = viewModel(), 
    usuarioId: Int,
    onLogout: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToCart: () -> Unit
) {
    val navController = rememberNavController()
    // Observa el estado del catálogo de productos desde TiendaViewModel.
    val listaProductos by tiendaViewModel.productos.observeAsState(initial = emptyList())
    // Observa el estado del total del carrito desde CarritoViewModel.
    val totalCarrito by carritoViewModel.total.collectAsState()

    // Efecto secundario para cargar el catálogo de productos una sola vez cuando la pantalla se muestra.
    LaunchedEffect(Unit) {
        tiendaViewModel.cargarTodosLosProductos()
    }

    // Delega la renderización de la UI al composable sin estado TiendaContent.
    TiendaContent(
        productos = listaProductos,
        totalCarrito = totalCarrito,
        onAgregar = { producto ->
            // La lógica de agregar al carrito se delega al CarritoViewModel.
            carritoViewModel.agregarProductoAlCarrito(producto, usuarioId)
        },
        onComprar = {},
        onNavigateToProfile = onNavigateToProfile,
        onLogout = onLogout,
        onNavigateToCart = onNavigateToCart,
        navController = navController
    )
}
