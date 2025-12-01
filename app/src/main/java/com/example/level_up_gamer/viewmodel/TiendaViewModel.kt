package com.example.level_up_gamer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.level_up_gamer.data.LevelUpDatabase
import com.example.level_up_gamer.model.DetallePedido
import com.example.level_up_gamer.model.Pedido
import com.example.level_up_gamer.model.Producto
import kotlinx.coroutines.launch
import java.util.Date

class TiendaViewModel(application: Application) : AndroidViewModel(application) {

    private val db = LevelUpDatabase.getDatabase(application)
    private val productoDao = db.productoDao()
    private val pedidoDao = db.pedidoDao()
    private val detalleDao = db.detallePedidoDao()

    private val _productos = MutableLiveData<List<Producto>>()
    val productos: LiveData<List<Producto>> get() = _productos

    private val carritoTemporal = mutableListOf<DetallePedido>()

    private val _totalCarrito = MutableLiveData(0.0)
    val totalCarrito: LiveData<Double> get() = _totalCarrito

    private val _compraExitosa = MutableLiveData<Boolean>()
    val compraExitosa: LiveData<Boolean> get() = _compraExitosa

    init {
        cargarTodosLosProductos()
    }

    fun cargarTodosLosProductos() {
        viewModelScope.launch {
            _productos.value = productoDao.obtenerTodos()
        }
    }

    fun filtrarPorCategoria(esVideojuego: Boolean) {
        viewModelScope.launch {
            _productos.value = productoDao.obtenerPorCategoria(esVideojuego)
        }
    }

    // CORREGIDO: Lógica para agregar productos al carrito
    fun agregarAlCarrito(producto: Producto, cantidad: Int = 1) {
        // Buscamos si el producto ya existe en el carrito
        val detalleExistente = carritoTemporal.find { it.productoId == producto.id }

        if (detalleExistente != null) {
            // Si existe, actualizamos su cantidad creando un objeto nuevo
            val indice = carritoTemporal.indexOf(detalleExistente)
            val detalleActualizado = detalleExistente.copy(cantidad = detalleExistente.cantidad + cantidad)
            carritoTemporal[indice] = detalleActualizado
        } else {
            // Si no existe, creamos un nuevo detalle y lo añadimos
            val nuevoDetalle = DetallePedido(
                    pedidoId = 0,
                    productoId = producto.id,
                    cantidad = cantidad,
                    precioUnitario = producto.precio
            )
            carritoTemporal.add(nuevoDetalle)
        }
        
        // Recalculamos el total para asegurar la consistencia
        recalcularTotalCarrito()
    }

    // NUEVO: Función de ayuda para tener un cálculo del total siempre preciso
    private fun recalcularTotalCarrito() {
        var nuevoTotal = 0.0
        carritoTemporal.forEach { detalle ->
            nuevoTotal += detalle.cantidad * detalle.precioUnitario
        }
        _totalCarrito.value = nuevoTotal
    }

    fun confirmarCompra(usuarioId: Int) {
        if (carritoTemporal.isEmpty()) return

                viewModelScope.launch {
            val nuevoPedido = Pedido(
                    usuarioId = usuarioId,
                    fecha = Date().toString(),
                    total = _totalCarrito.value ?: 0.0,
                    estado = "PAGADO"
            )

            val pedidoIdLong = pedidoDao.insertar(nuevoPedido)
            val pedidoIdInt = pedidoIdLong.toInt()

            val detallesFinales = carritoTemporal.map { detalle ->
                    detalle.copy(pedidoId = pedidoIdInt)
            }

            detalleDao.insertarVarios(detallesFinales)

            carritoTemporal.clear()
            _totalCarrito.value = 0.0
            _compraExitosa.value = true
        }
    }
}
