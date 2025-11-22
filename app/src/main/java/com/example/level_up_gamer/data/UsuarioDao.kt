package com.example.level_up_gamer.data

import androidx.room.Insert
import com.example.level_up_gamer.model.Usuario

interface UsuarioDao {
    @Insert
    suspend fun insertar(usuario : Usuario)
}