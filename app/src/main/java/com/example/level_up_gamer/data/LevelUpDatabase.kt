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
    // 1. Aquí exponemos los DAOs que creamos
    abstract fun usuarioDao(): UsuarioDao
    abstract fun productoDao(): ProductoDao
    abstract fun pedidoDao(): PedidoDao
    abstract fun detallePedidoDao(): DetallePedidoDao

    companion object {
        @Volatile
        private var INSTANCE: LevelUpDatabase? = null

        fun getDatabase(context: Context): LevelUpDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LevelUpDatabase::class.java,
                    "levelup_gamer.db" // 2. Nombre del archivo de base de datos
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback()) // 3. Llamamos al callback para poblar datos
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // 4. Función separada para limpiar el código y rellenar con datos de Videojuegos/Juegos de Mesa
        suspend fun insertarDatosPorDefecto(db: LevelUpDatabase) {
            val usuarioDao = db.usuarioDao()
            val productoDao = db.productoDao()

            // --- Insertar Usuarios de Prueba ---
            val usuarios = listOf(
                Usuario(nombre = "Admin", correo = "admin@levelup.com", contrasena = "admin123"),
                Usuario(nombre = "Jugador1", correo = "player1@gmail.com", contrasena = "1234")
            )
            usuarios.forEach { usuarioDao.insertar(it) }

            // --- Insertar Productos de Prueba ---
            val productos = listOf(
                // Videojuegos
                Producto(
                    nombre = "The Legend of Zelda: TOTK",
                    descripcion = "Una aventura épica en Hyrule.",
                    precio = 59990.0,
                    stock = 20,
                    esVideojuego = true,
                    imagenResName = "zelda" // Debe coincidir con el nombre del archivo sin extensión
                ),
                Producto(
                    nombre = "God of War: Ragnarok",
                    descripcion = "Kratos y Atreus enfrentan el fin del mundo.",
                    precio = 64990.0,
                    stock = 15,
                    esVideojuego = true,
                    imagenResName = "god_of_war"
                ),
                // Juegos de Mesa
                Producto(
                    nombre = "Catan",
                    descripcion = "El juego de estrategia y comercio.",
                    precio = 35990.0,
                    stock = 10,
                    esVideojuego = false,
                    imagenResName = "catan" // Aquí va tu catan.jpg (sin la extensión)
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

    // Clase interna para manejar el evento de creación de la BD
    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Esto asegura que la base de datos no esté vacía al iniciar
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    insertarDatosPorDefecto(database)
                }
            }
        }
    }
}
