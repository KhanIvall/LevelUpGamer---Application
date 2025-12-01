# Level Up - E-commerce de Gaming

Una aplicación móvil de e-commerce desarrollada 100% en Kotlin con Jetpack Compose, especializada en la venta de videojuegos y juegos de mesa.

## Descripción del Proyecto

Level Up es una tienda virtual diseñada para ofrecer una experiencia de usuario fluida y temática para entusiastas del gaming. La aplicación permite a los clientes explorar un catálogo visual, gestionar un carrito de compras persistente y administrar su perfil de usuario de forma segura e intuitiva.

**Desarrollado por:** 
* Ari Araya
* Giannina Guerrero

---

## Funcionalidades Implementadas

### Autenticación y Gestión de Usuarios
* **Login:** Autenticación de usuarios existentes contra la base de datos local.
* **Registro:** Creación de nuevas cuentas con validaciones.
* **Perfil:** Visualización y edición de datos personales del usuario logueado (nombre, correo).

### Catálogo y Productos
* **Home Screen (Tienda):** Dashboard principal que muestra el catálogo en un `LazyVerticalGrid`.
* **Filtros por Categoría:** Navegación dinámica (Videojuegos / Juegos de Mesa).
* **Imágenes:** Visualización de imágenes de productos utilizando la librería Coil.

### Carrito de Compras
* **Persistencia:** Gestión mediante Room Database; el carrito no se pierde al cerrar la app (tablas Pedido y DetallePedido).
* **Agregación Inteligente:** Al añadir un producto, el sistema actualiza la cantidad si ya existe, evitando duplicados.
* **Cálculo en Tiempo Real:** El total se actualiza instantáneamente en la UI.
* **Visualización:** Pantalla dedicada con el resumen de productos, cantidades y total a pagar.

---

## Tecnologías y Arquitectura

* **Lenguaje:** Kotlin (Corrutinas, Flow).
* **UI:** Jetpack Compose + Material Design 3.
* **Arquitectura:** MVVM (Model-View-ViewModel).
* **Persistencia:** Room Database (SQLite).
* **Navegación:** Navigation Component for Compose.
* **Patrones de Diseño:** Repository Pattern, Observer, Singleton, State Hoisting.

---

## Estructura del Proyecto

```kotlin
app/
├── data/
│   ├── LevelUpDatabase.kt     // BD
│   ├── PedidoDao.kt
│   ├── DetallePedidoDao.kt
│   ├── ProductoDao.kt
│   └── UsuarioDao.kt
├── model/
│   ├── Usuario.kt             // Entidad
│   ├── Producto.kt            // Entidad
│   ├── Pedido.kt              // Entidad (Cabecera del carrito)
│   └── DetallePedido.kt       // Entidad (Items del carrito)
├── viewmodel/
│   ├── CarritoViewModel.kt    // Lógica del carrito
│   ├── TiendaViewModel.kt     // Lógica del catálogo
│   ├── LoginViewModel.kt
│   └── ProfileViewModel.kt
├── ui/
│   ├── screen/                // Pantallas
│   └── components/            // Composables reutilizables)
└── navigation/
    └── AppNavigation.kt       

