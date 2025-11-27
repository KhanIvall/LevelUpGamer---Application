package com.example.level_up_gamer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Producto")
data class Producto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val stock: Int,
    val imagenUrl: String? = null, // URL o ruta de la imagen
    val esVideojuego: Boolean = true // Para diferenciar entre videojuego y juego de mesa
)