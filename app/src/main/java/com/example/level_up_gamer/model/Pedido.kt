package com.example.level_up_gamer.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Pedido",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE // Si se borra el usuario, se borran sus pedidos
        )
    ]
)
data class Pedido(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val usuarioId: Int, // Clave foránea
    val fecha: String, // Podrías usar Long para timestamps
    val total: Double,
    val estado: String = "PENDIENTE" // Ej: PENDIENTE, PAGADO, ENVIADO
)