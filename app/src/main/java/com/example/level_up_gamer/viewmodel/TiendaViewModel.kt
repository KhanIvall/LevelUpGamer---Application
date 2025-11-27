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
import java.util.Date // O usar java.time si tu API level lo permite

class TiendaViewModel(application: Application) : AndroidViewModel(application) {

    private val db = LevelUpDatabase.getDatabase(application)
    private val productoDao = db.productoDao()
    private val pedidoDao = db.pedidoDao()
    private val detalleDao = db.detallePedidoDao()

    // Lista de productos que se muestra en pantalla
    private val _productos = MutableLiveData<List<Producto>>()
    val productos: LiveData<List<Producto>> get() = _productos

    // Carrito temporal (Lista de pares: Producto + Cantidad)
    private val carritoTemporal = mutableListOf<DetallePedido>()

    // Total a pagar observable
    private val _totalCarrito = MutableLiveData(0.0)
    val totalCarrito: LiveData<Double> get() = _totalCarrito

    // Mensaje de éxito al comprar
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

    // Función para agregar un producto al "carrito" (memoria ram)
    fun agregarAlCarrito(producto: Producto, cantidad: Int = 1) {
        // Creamos un objeto Detalle temporal (aún sin pedidoId real)
        val detalle = DetallePedido(
                pedidoId = 0, // Se asignará al confirmar la compra
                productoId = producto.id,
                cantidad = cantidad,
                precioUnitario = producto.precio
        )
        carritoTemporal.add(detalle)

        // Actualizamos el total
        val totalActual = _totalCarrito.value ?: 0.0
        _totalCarrito.value = totalActual + (producto.precio * cantidad)
    }

    // Función final: Guarda todo en la Base de Datos
    fun confirmarCompra(usuarioId: Int) {
        if (carritoTemporal.isEmpty())
            return
            viewModelScope.launch {
            // 1. Crear el Pedido (Cabecera)
            val nuevoPedido = Pedido(
                    usuarioId = usuarioId,
                    fecha = Date().toString(), // Guardamos fecha actual
                    total = _totalCarrito.value ?: 0.0,
                    estado = "PAGADO"
            )

            // Insertamos y obtenemos el ID generado automáticamente
            val pedidoIdLong = pedidoDao.insertar(nuevoPedido)
            val pedidoIdInt = pedidoIdLong.toInt()

            // 2. Asignar ese ID a los detalles y guardarlos
            val detallesFinales = carritoTemporal.map { detalle ->
                    detalle.copy(pedidoId = pedidoIdInt)
            }

            // Insertamos la lista de detalles
            detalleDao.insertarVarios(detallesFinales)

            // 3. Limpiar carrito y avisar a la vista
            carritoTemporal.clear()
            _totalCarrito.value = 0.0
            _compraExitosa.value = true
        }
    }
}