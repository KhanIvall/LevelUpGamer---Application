package com.example.level_up_gamer.data

import androidx.room.*
import com.example.level_up_gamer.model.DetallePedido

@Dao
interface DetallePedidoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(detalle: DetallePedido)

    @Insert
    suspend fun insertarVarios(detalles: List<DetallePedido>)

    @Update
    suspend fun actualizar(detalle: DetallePedido)

    @Delete
    suspend fun eliminar(detalle: DetallePedido)

    @Query("SELECT * FROM DetallePedido WHERE pedidoId = :pedidoId")
    suspend fun obtenerPorPedido(pedidoId: Int): List<DetallePedido>

    @Query("SELECT * FROM DetallePedido WHERE pedidoId = :pedidoId AND productoId = :productoId LIMIT 1")
    suspend fun obtenerDetallePorProducto(pedidoId: Int, productoId: Int): DetallePedido?
}