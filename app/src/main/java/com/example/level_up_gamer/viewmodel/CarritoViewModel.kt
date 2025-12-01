package com.example.level_up_gamer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.level_up_gamer.data.LevelUpDatabase
import com.example.level_up_gamer.model.DetallePedido
import com.example.level_up_gamer.model.Pedido
import com.example.level_up_gamer.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CarritoViewModel(application: Application) : AndroidViewModel(application) {

    private val pedidoDao = LevelUpDatabase.getDatabase(application).pedidoDao()
    private val detallePedidoDao = LevelUpDatabase.getDatabase(application).detallePedidoDao()
    private val productoDao = LevelUpDatabase.getDatabase(application).productoDao()

    private val _total = MutableStateFlow(0.0)
    val total: StateFlow<Double> = _total.asStateFlow()

    private val _itemsDelCarrito = MutableStateFlow<List<Pair<Producto, DetallePedido>>>(emptyList())
    val itemsDelCarrito: StateFlow<List<Pair<Producto, DetallePedido>>> = _itemsDelCarrito.asStateFlow()

    /**
     * Carga el contenido del carrito activo de un usuario específico.
     */
    fun cargarCarritoDelUsuario(usuarioId: Int) {
        viewModelScope.launch {
            // Busca el pedido que funciona como carrito activo
            val carritoActivo = pedidoDao.obtenerCarritoActivoPorUsuario(usuarioId)
            if (carritoActivo != null) {
                // Si lo encuentra, carga todos sus items (productos y detalles)
                cargarItemsDelCarrito(carritoActivo.id)
            } else {
                // Si no hay carrito, nos aseguramos de que la UI muestre un estado vacío
                _itemsDelCarrito.value = emptyList()
                _total.value = 0.0
            }
        }
    }

    fun agregarProductoAlCarrito(producto: Producto, usuarioId: Int, cantidad: Int = 1) {
        viewModelScope.launch {
            val carrito = obtenerOCrearCarrito(usuarioId)
            val detalleExistente = detallePedidoDao.obtenerDetallePorProducto(carrito.id, producto.id)

            if (detalleExistente != null) {
                val detalleActualizado = detalleExistente.copy(cantidad = detalleExistente.cantidad + cantidad)
                detallePedidoDao.actualizar(detalleActualizado)
            } else {
                val nuevoDetalle = DetallePedido(
                    pedidoId = carrito.id,
                    productoId = producto.id,
                    cantidad = cantidad,
                    precioUnitario = producto.precio
                )
                detallePedidoDao.insertar(nuevoDetalle)
            }
            cargarItemsDelCarrito(carrito.id)
        }
    }

    private fun cargarItemsDelCarrito(pedidoId: Int) {
        viewModelScope.launch {
            val detalles = detallePedidoDao.obtenerPorPedido(pedidoId)
            var sumaTotal = 0.0
            val itemsConProducto = mutableListOf<Pair<Producto, DetallePedido>>()

            detalles.forEach { detalle ->
                productoDao.obtenerPorId(detalle.productoId)?.let { producto ->
                    itemsConProducto.add(producto to detalle)
                    sumaTotal += detalle.cantidad * detalle.precioUnitario
                }
            }
            _itemsDelCarrito.value = itemsConProducto
            _total.value = sumaTotal
        }
    }

    private suspend fun obtenerOCrearCarrito(usuarioId: Int): Pedido {
        val carritoActivo = pedidoDao.obtenerCarritoActivoPorUsuario(usuarioId)
        if (carritoActivo != null) {
            return carritoActivo
        }
        val nuevoPedido = Pedido(usuarioId = usuarioId, estado = "CARRITO")
        val nuevoPedidoId = pedidoDao.insertar(nuevoPedido)
        return Pedido(id = nuevoPedidoId.toInt(), usuarioId = usuarioId, estado = "CARRITO")
    }
}