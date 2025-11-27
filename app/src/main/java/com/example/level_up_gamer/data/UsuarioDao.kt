package com.example.level_up_gamer.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.level_up_gamer.model.Usuario

@Dao
interface UsuarioDao {
    @Insert
    suspend fun insertar(usuario: Usuario)

    @Update
    suspend fun actualizar(usuario: Usuario)

    @Delete
    suspend fun eliminar(usuario: Usuario)

    @Query("SELECT * FROM usuario")
    suspend fun obtenerTodos(): List<Usuario>

    @Query("SELECT * FROM usuario WHERE id = :id")
    suspend fun obtenerPorId(id: Int): Usuario?

    @Query("SELECT * FROM usuario WHERE nombre = :nombre AND contrasena = :contrasena")
    suspend fun login(nombre: String, contrasena: String): Usuario?
}