package com.example.level_up_gamer.data

import androidx.room.*
import com.example.level_up_gamer.model.Producto

@Dao
interface ProductoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(producto: Producto)

    @Update
    suspend fun actualizar(producto: Producto)

    @Delete
    suspend fun eliminar(producto: Producto)

    @Query("SELECT * FROM Producto")
    suspend fun obtenerTodos(): List<Producto>

    @Query("SELECT * FROM Producto WHERE id = :id")
    suspend fun obtenerPorId(id: Int): Producto?

    @Query("SELECT * FROM Producto WHERE esVideojuego = :esVideojuego")
    suspend fun obtenerPorCategoria(esVideojuego: Boolean): List<Producto>
}