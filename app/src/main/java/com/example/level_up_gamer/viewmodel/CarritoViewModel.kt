package com.example.level_up_gamer.viewmodel

import androidx.lifecycle.ViewModel
import com.example.level_up_gamer.model.DetallePedido
import com.example.level_up_gamer.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Gestiona el estado del carrito de compras exclusivamente en memoria.
 * El contenido del carrito se pierde cuando la aplicación se cierra.
 */
class CarritoViewModel : ViewModel() {

    private val _itemsDelCarrito = MutableStateFlow<List<Pair<Producto, DetallePedido>>>(emptyList())
    val itemsDelCarrito: StateFlow<List<Pair<Producto, DetallePedido>>> = _itemsDelCarrito.asStateFlow()

    private val _total = MutableStateFlow(0.0)
    val total: StateFlow<Double> = _total.asStateFlow()

    /**
     * Añade un producto al carrito. Si ya existe, actualiza su cantidad.
     */
    fun agregarProductoAlCarrito(producto: Producto, cantidad: Int = 1) {
        val itemsActuales = _itemsDelCarrito.value.toMutableList()
        val itemExistente = itemsActuales.find { it.first.id == producto.id }

        if (itemExistente != null) {
            // El producto ya está en el carrito, se actualiza la cantidad.
            val indice = itemsActuales.indexOf(itemExistente)
            val detalleActualizado = itemExistente.second.copy(cantidad = itemExistente.second.cantidad + cantidad)
            itemsActuales[indice] = itemExistente.first to detalleActualizado
        } else {
            // El producto es nuevo, se añade a la lista.
            val nuevoDetalle = DetallePedido(
                pedidoId = 0, // No relevante en la versión en memoria
                productoId = producto.id,
                cantidad = cantidad,
                precioUnitario = producto.precio
            )
            itemsActuales.add(producto to nuevoDetalle)
        }

        _itemsDelCarrito.value = itemsActuales
        recalcularTotal()
    }

    /**
     * Vacía el carrito de compras.
     */
    fun limpiarCarrito() {
        _itemsDelCarrito.value = emptyList()
        _total.value = 0.0
    }

    /**
     * Recalcula el valor total del carrito basado en su contenido actual.
     */
    private fun recalcularTotal() {
        var sumaTotal = 0.0
        _itemsDelCarrito.value.forEach { (_, detalle) ->
            sumaTotal += detalle.cantidad * detalle.precioUnitario
        }
        _total.value = sumaTotal
    }
}