package com.example.level_up_gamer.model

import androidx.room.Entity


@Entity(tableName = 'Usuario')
data class Usuario (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val correo: String,
    val contrasena: String

)

