package com.example.level_up_gamer.data

import androidx.room.*
import com.example.level_up_gamer.model.DetallePedido

@Dao
interface DetallePedidoDao {
    @Insert
    suspend fun insertar(detalle: DetallePedido)

    @Insert
    suspend fun insertarVarios(detalles: List<DetallePedido>) // Para insertar la lista completa de una vez

    @Query("SELECT * FROM DetallePedido WHERE pedidoId = :pedidoId")
    suspend fun obtenerPorPedido(pedidoId: Int): List<DetallePedido>
}