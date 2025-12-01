package com.example.level_up_gamer.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.level_up_gamer.model.DetallePedido
import com.example.level_up_gamer.model.Pedido
import com.example.level_up_gamer.model.Producto
import com.example.level_up_gamer.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Usuario::class,
        Producto::class,
        Pedido::class,
        DetallePedido::class
    ],
    version = 3
)
abstract class LevelUpDatabase: RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun productoDao(): ProductoDao
    abstract fun pedidoDao(): PedidoDao
    abstract fun detallePedidoDao(): DetallePedidoDao

    companion object {
        @Volatile
        private var INSTANCE: LevelUpDatabase? = null

        fun getDatabase(context: Context): LevelUpDatabase {
            // Se utiliza un singleton para prevenir múltiples instancias de la BD.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LevelUpDatabase::class.java,
                    "levelup_gamer.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback()) // Popula la BD en la primera creación.
                    .build()
                INSTANCE = instance
                instance
            }
        }
        
        // Función para poblar la base de datos con datos de prueba.
        private suspend fun insertarDatosPorDefecto(db: LevelUpDatabase) {
            val usuarioDao = db.usuarioDao()
            val productoDao = db.productoDao()

            val usuarios = listOf(
                Usuario(nombre = "Admin", correo = "admin@levelup.com", contrasena = "admin123"),
                Usuario(nombre = "Jugador1", correo = "player1@gmail.com", contrasena = "1234")
            )
            usuarios.forEach { usuarioDao.insertar(it) }

            val productos = listOf(
                Producto(
                    nombre = "The Legend of Zelda: TOTK",
                    descripcion = "Una aventura épica en Hyrule.",
                    precio = 59990.0,
                    stock = 20,
                    esVideojuego = true,
                    imagenResName = "zelda"
                ),
                Producto(
                    nombre = "God of War: Ragnarok",
                    descripcion = "Kratos y Atreus enfrentan el fin del mundo.",
                    precio = 64990.0,
                    stock = 15,
                    esVideojuego = true,
                    imagenResName = "god_of_war"
                ),
                Producto(
                    nombre = "Catan",
                    descripcion = "El juego de estrategia y comercio.",
                    precio = 35990.0,
                    stock = 10,
                    esVideojuego = false,
                    imagenResName = "catan"
                ),
                Producto(
                    nombre = "Dixit",
                    descripcion = "Un juego de imaginación y cartas ilustradas.",
                    precio = 29990.0,
                    stock = 8,
                    esVideojuego = false,
                    imagenResName = "dixit"
                )
            )
            productos.forEach { productoDao.insertar(it) }
        }
    }

    // Callback para poblar la base de datos la primera vez que se crea.
    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    insertarDatosPorDefecto(database)
                }
            }
        }
    }
}