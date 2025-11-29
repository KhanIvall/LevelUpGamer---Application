package com.example.level_up_gamer.ui.screen

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import com.example.level_up_gamer.ui.components.TiendaContent
import com.example.level_up_gamer.viewmodel.TiendaViewModel

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