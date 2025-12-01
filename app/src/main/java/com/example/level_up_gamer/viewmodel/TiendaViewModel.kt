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

    // Carrito temporal que vive solo en la memoria del ViewModel.
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

    /**
     * Añade un producto al carrito temporal. 
     * Si el producto ya existe, solo actualiza su cantidad.
     */
    fun agregarAlCarrito(producto: Producto, cantidad: Int = 1) {
        // Busca si el producto ya está en el carrito para no duplicarlo.
        val detalleExistente = carritoTemporal.find { it.productoId == producto.id }

        if (detalleExistente != null) {
            // Si existe, actualiza la cantidad creando un objeto nuevo e inmutable.
            val indice = carritoTemporal.indexOf(detalleExistente)
            val detalleActualizado = detalleExistente.copy(cantidad = detalleExistente.cantidad + cantidad)
            carritoTemporal[indice] = detalleActualizado
        } else {
            // Si es un producto nuevo, crea el detalle y lo añade a la lista.
            val nuevoDetalle = DetallePedido(
                    pedidoId = 0, // ID temporal, el real se asigna al confirmar la compra.
                    productoId = producto.id,
                    cantidad = cantidad,
                    precioUnitario = producto.precio
            )
            carritoTemporal.add(nuevoDetalle)
        }
        
        recalcularTotalCarrito()
    }

    /**
     * Recalcula el total iterando la lista completa para garantizar la precisión del dato.
     */
    private fun recalcularTotalCarrito() {
        var nuevoTotal = 0.0
        carritoTemporal.forEach { detalle ->
            nuevoTotal += detalle.cantidad * detalle.precioUnitario
        }
        _totalCarrito.value = nuevoTotal
    }

    /**
     * Persiste el carrito temporal en la base de datos como una orden final.
     */
    fun confirmarCompra(usuarioId: Int) {
        if (carritoTemporal.isEmpty()) return

        viewModelScope.launch {
            val nuevoPedido = Pedido(
                    usuarioId = usuarioId,
                    fecha = Date().toString(),
                    total = _totalCarrito.value ?: 0.0,
                    estado = "PAGADO"
            )

            // Inserta el pedido y obtiene el ID real generado por la base de datos.
            val pedidoIdLong = pedidoDao.insertar(nuevoPedido)
            val pedidoIdInt = pedidoIdLong.toInt()

            // Actualiza cada detalle con el ID del pedido real antes de guardarlos.
            val detallesFinales = carritoTemporal.map { detalle ->
                    detalle.copy(pedidoId = pedidoIdInt)
            }

            detalleDao.insertarVarios(detallesFinales)

            // Limpia el estado temporal después de una compra exitosa.
            carritoTemporal.clear()
            _totalCarrito.value = 0.0
            _compraExitosa.value = true
        }
    }
}