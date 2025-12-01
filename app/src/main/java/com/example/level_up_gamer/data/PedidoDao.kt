package com.example.level_up_gamer.data

import androidx.room.*
import com.example.level_up_gamer.model.Pedido

@Dao
interface PedidoDao {
    /**
     * Inserta un nuevo pedido en la base de datos.
     * @return El ID del pedido recién insertado.
     */
    @Insert
    suspend fun insertar(pedido: Pedido): Long

    @Update
    suspend fun actualizar(pedido: Pedido)

    @Query("SELECT * FROM Pedido WHERE usuarioId = :usuarioId ORDER BY id DESC")
    suspend fun obtenerPedidosPorUsuario(usuarioId: Int): List<Pedido>

    @Query("SELECT * FROM Pedido WHERE usuarioId = :usuarioId AND estado = 'CARRITO' LIMIT 1")
    suspend fun obtenerCarritoActivoPorUsuario(usuarioId: Int): Pedido?
}