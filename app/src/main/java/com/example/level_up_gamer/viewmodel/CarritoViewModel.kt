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

    // Este stateflow combinará los detalles con la información del producto para la UI
    private val _itemsDelCarrito = MutableStateFlow<List<Pair<Producto, DetallePedido>>>(emptyList())
    val itemsDelCarrito: StateFlow<List<Pair<Producto, DetallePedido>>> = _itemsDelCarrito.asStateFlow()

    /**
     * Añade un producto al carrito del usuario activo.
     */
    fun agregarProductoAlCarrito(producto: Producto, usuarioId: Int, cantidad: Int = 1) {
        viewModelScope.launch {
            // 1. Buscar o crear un carrito activo para el usuario
            val carrito = obtenerOCrearCarrito(usuarioId)

            // 2. Buscar si el producto ya existe en el carrito
            val detalleExistente = detallePedidoDao.obtenerDetallePorProducto(carrito.id, producto.id)

            if (detalleExistente != null) {
                // 3. Si existe, crear una copia con la cantidad actualizada
                val detalleActualizado = detalleExistente.copy(cantidad = detalleExistente.cantidad + cantidad)
                detallePedidoDao.actualizar(detalleActualizado)
            } else {
                // 4. Si no existe, crear un nuevo detalle
                val nuevoDetalle = DetallePedido(
                    pedidoId = carrito.id,
                    productoId = producto.id,
                    cantidad = cantidad,
                    precioUnitario = producto.precio
                )
                detallePedidoDao.insertar(nuevoDetalle)
            }
            
            // 5. Refrescar la lista de items y el total para la UI
            cargarItemsDelCarrito(carrito.id)
        }
    }

    /**
     * Carga los items del carrito y calcula el total. 
     * Se debe llamar cada vez que el carrito cambie.
     */
    fun cargarItemsDelCarrito(pedidoId: Int) {
        viewModelScope.launch {
            val detalles = detallePedidoDao.obtenerPorPedido(pedidoId)
            var sumaTotal = 0.0
            val itemsConProducto = mutableListOf<Pair<Producto, DetallePedido>>()

            detalles.forEach { detalle ->
                // Buscamos el producto completo para obtener nombre, imagen, etc.
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
        // Si no hay carrito, creamos uno nuevo
        val nuevoPedido = Pedido(usuarioId = usuarioId, estado = "CARRITO")
        val nuevoPedidoId = pedidoDao.insertar(nuevoPedido)
        return Pedido(id = nuevoPedidoId.toInt(), usuarioId = usuarioId, estado = "CARRITO")
    }
}
