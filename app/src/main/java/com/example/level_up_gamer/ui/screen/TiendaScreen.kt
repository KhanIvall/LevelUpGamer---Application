package com.example.level_up_gamer.ui.screen

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.level_up_gamer.ui.components.TiendaContent
import com.example.level_up_gamer.viewmodel.CarritoViewModel
import com.example.level_up_gamer.viewmodel.TiendaViewModel

@Composable
fun TiendaScreen(
    tiendaViewModel: TiendaViewModel, 
    carritoViewModel: CarritoViewModel = viewModel(), 
    usuarioId: Int,
    onLogout: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToCart: () -> Unit
) {
    val listaProductos by tiendaViewModel.productos.observeAsState(initial = emptyList())
    val totalCarrito by carritoViewModel.total.collectAsState()

    LaunchedEffect(Unit) {
        tiendaViewModel.cargarTodosLosProductos()
    }

    TiendaContent(
        productos = listaProductos,
        totalCarrito = totalCarrito,
        onAgregar = { producto ->
            carritoViewModel.agregarProductoAlCarrito(producto, usuarioId)
        },
        onComprar = {},
        onNavigateToProfile = onNavigateToProfile,
        onLogout = onLogout,
        onNavigateToCart = onNavigateToCart
    )
}
