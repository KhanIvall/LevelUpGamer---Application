package com.example.level_up_gamer.ui.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.MutableLiveData
import com.example.level_up_gamer.model.Producto
import com.example.level_up_gamer.viewmodel.CarritoViewModel
import com.example.level_up_gamer.viewmodel.TiendaViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun tiendaScreen_muestraTitulo(){

        // Mock de TiendaViewModel
        val tiendaViewModel = mockk<TiendaViewModel>(relaxed = true) {
            every { productos } returns MutableLiveData(emptyList())
            every { totalCarrito } returns MutableLiveData(0.0)
            every { compraExitosa } returns MutableLiveData(false)
        }

        // Mock de CarritoViewModel
        val carritoViewModel = mockk<CarritoViewModel>(relaxed = true) {
            every { total } returns MutableStateFlow(0.0)
        }

        composeTestRule.setContent {
            TiendaScreen(
                tiendaViewModel = tiendaViewModel,
                carritoViewModel = carritoViewModel,
                usuarioId = 1,
                onLogout = { },
                onNavigateToProfile = { },
                onNavigateToCart = { }
            )
        }
        composeTestRule.onNodeWithText("TIENDA LEVEL UP").assertIsDisplayed()
    }
    @Test
    fun homeScreen_muestraMensajeCargandoCuandoNoHayProductos() {
        val tiendaViewModel = mockk<TiendaViewModel>(relaxed = true) {
            every { productos } returns MutableLiveData(emptyList())
            every { totalCarrito } returns MutableLiveData(0.0)
        }

        val carritoViewModel = mockk<CarritoViewModel>(relaxed = true) {
            every { total } returns MutableStateFlow(0.0)
        }

        composeTestRule.setContent {
            TiendaScreen(
                tiendaViewModel = tiendaViewModel,
                carritoViewModel = carritoViewModel,
                usuarioId = 123,
                onLogout = { },
                onNavigateToProfile = { },
                onNavigateToCart = { }
            )
        }

        composeTestRule.onNodeWithText("Cargando inventario...").assertIsDisplayed()
    }

    @Test
    fun homeScreen_muestraProductosCuandoExisten() {
        val productosTest = listOf(
            Producto(id = 1, nombre = "God of War", descripcion = "El mejor juego", precio = 59990.0, stock = 10, esVideojuego = true),
            Producto(id = 2, nombre = "PlayStation 5", descripcion = "La mejor consola", precio = 499990.0, stock = 5, esVideojuego = false)
        )

        val tiendaViewModel = mockk<TiendaViewModel>(relaxed = true) {
            every { productos } returns MutableLiveData(productosTest)
            every { totalCarrito } returns MutableLiveData(0.0)
        }

        val carritoViewModel = mockk<CarritoViewModel>(relaxed = true) {
            every { total } returns MutableStateFlow(0.0)
        }

        composeTestRule.setContent {
            TiendaScreen(
                tiendaViewModel = tiendaViewModel,
                carritoViewModel = carritoViewModel,
                usuarioId = 123,
                onLogout = { },
                onNavigateToProfile = { },
                onNavigateToCart = { }
            )
        }

        composeTestRule.onNodeWithText("God of War").assertIsDisplayed()
        composeTestRule.onNodeWithText("PlayStation 5").assertIsDisplayed()
    }

    @Test
    fun homeScreen_muestraTotalCarritoEnCero() {
        val tiendaViewModel = mockk<TiendaViewModel>(relaxed = true) {
            every { productos } returns MutableLiveData(emptyList())
            every { totalCarrito } returns MutableLiveData(0.0)
        }

        val carritoViewModel = mockk<CarritoViewModel>(relaxed = true) {
            every { total } returns MutableStateFlow(0.0)
        }

        composeTestRule.setContent {
            TiendaScreen(
                tiendaViewModel = tiendaViewModel,
                carritoViewModel = carritoViewModel,
                usuarioId = 123,
                onLogout = { },
                onNavigateToProfile = { },
                onNavigateToCart = { }
            )
        }

        composeTestRule.onNodeWithText("Total a Pagar:").assertIsDisplayed()
        composeTestRule.onNodeWithText("$0").assertIsDisplayed()
    }

    @Test
    fun homeScreen_muestraTotalCarritoConValor() {
        val tiendaViewModel = mockk<TiendaViewModel>(relaxed = true) {
            every { productos } returns MutableLiveData(emptyList())
            every { totalCarrito } returns MutableLiveData(0.0)
        }

        val carritoViewModel = mockk<CarritoViewModel>(relaxed = true) {
            every { total } returns MutableStateFlow(59990.0)
        }

        composeTestRule.setContent {
            TiendaScreen(
                tiendaViewModel = tiendaViewModel,
                carritoViewModel = carritoViewModel,
                usuarioId = 123,
                onLogout = { },
                onNavigateToProfile = { },
                onNavigateToCart = { }
            )
        }

        composeTestRule.onNodeWithText("$59.990", substring = true).assertIsDisplayed()
    }

    @Test
    fun homeScreen_botonPagarDeshabilitadoCuandoTotalEsCero() {
        val tiendaViewModel = mockk<TiendaViewModel>(relaxed = true) {
            every { productos } returns MutableLiveData(emptyList())
            every { totalCarrito } returns MutableLiveData(0.0)
        }

        val carritoViewModel = mockk<CarritoViewModel>(relaxed = true) {
            every { total } returns MutableStateFlow(0.0)
        }

        composeTestRule.setContent {
            TiendaScreen(
                tiendaViewModel = tiendaViewModel,
                carritoViewModel = carritoViewModel,
                usuarioId = 123,
                onLogout = { },
                onNavigateToProfile = { },
                onNavigateToCart = { }
            )
        }

        composeTestRule.onNodeWithText("PAGAR").assertIsNotEnabled()
    }

    @Test
    fun homeScreen_botonPagarHabilitadoCuandoHayTotal() {
        val tiendaViewModel = mockk<TiendaViewModel>(relaxed = true) {
            every { productos } returns MutableLiveData(emptyList())
            every { totalCarrito } returns MutableLiveData(0.0)
        }

        val carritoViewModel = mockk<CarritoViewModel>(relaxed = true) {
            every { total } returns MutableStateFlow(100.0)
        }

        composeTestRule.setContent {
            TiendaScreen(
                tiendaViewModel = tiendaViewModel,
                carritoViewModel = carritoViewModel,
                usuarioId = 123,
                onLogout = { },
                onNavigateToProfile = { },
                onNavigateToCart = { }
            )
        }

        composeTestRule.onNodeWithText("PAGAR").assertIsEnabled()
    }

    @Test
    fun homeScreen_muestraIconosDeNavegacion() {
        val tiendaViewModel = mockk<TiendaViewModel>(relaxed = true) {
            every { productos } returns MutableLiveData(emptyList())
            every { totalCarrito } returns MutableLiveData(0.0)
        }

        val carritoViewModel = mockk<CarritoViewModel>(relaxed = true) {
            every { total } returns MutableStateFlow(0.0)
        }

        composeTestRule.setContent {
            TiendaScreen(
                tiendaViewModel = tiendaViewModel,
                carritoViewModel = carritoViewModel,
                usuarioId = 123,
                onLogout = { },
                onNavigateToProfile = { },
                onNavigateToCart = { }
            )
        }

        composeTestRule.onNodeWithContentDescription("Carrito").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Perfil").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Cerrar Sesión").assertIsDisplayed()
    }

    @Test
    fun homeScreen_clickEnCarritoLlamaCallback() {
        var navegoACarrito = false

        val tiendaViewModel = mockk<TiendaViewModel>(relaxed = true) {
            every { productos } returns MutableLiveData(emptyList())
            every { totalCarrito } returns MutableLiveData(0.0)
        }

        val carritoViewModel = mockk<CarritoViewModel>(relaxed = true) {
            every { total } returns MutableStateFlow(0.0)
        }

        composeTestRule.setContent {
            TiendaScreen(
                tiendaViewModel = tiendaViewModel,
                carritoViewModel = carritoViewModel,
                usuarioId = 123,
                onLogout = { },
                onNavigateToProfile = { },
                onNavigateToCart = { navegoACarrito = true }
            )
        }

        composeTestRule.onNodeWithContentDescription("Carrito").performClick()
        assert(navegoACarrito)
    }

    @Test
    fun homeScreen_clickEnPerfilLlamaCallback() {
        var navegoAPerfil = false

        val tiendaViewModel = mockk<TiendaViewModel>(relaxed = true) {
            every { productos } returns MutableLiveData(emptyList())
            every { totalCarrito } returns MutableLiveData(0.0)
        }

        val carritoViewModel = mockk<CarritoViewModel>(relaxed = true) {
            every { total } returns MutableStateFlow(0.0)
        }

        composeTestRule.setContent {
            TiendaScreen(
                tiendaViewModel = tiendaViewModel,
                carritoViewModel = carritoViewModel,
                usuarioId = 123,
                onLogout = { },
                onNavigateToProfile = { navegoAPerfil = true },
                onNavigateToCart = { }
            )
        }

        composeTestRule.onNodeWithContentDescription("Perfil").performClick()
        assert(navegoAPerfil)
    }

    @Test
    fun homeScreen_clickEnLogoutLlamaCallback() {
        var cerroSesion = false

        val tiendaViewModel = mockk<TiendaViewModel>(relaxed = true) {
            every { productos } returns MutableLiveData(emptyList())
            every { totalCarrito } returns MutableLiveData(0.0)
        }

        val carritoViewModel = mockk<CarritoViewModel>(relaxed = true) {
            every { total } returns MutableStateFlow(0.0)
        }

        composeTestRule.setContent {
            TiendaScreen(
                tiendaViewModel = tiendaViewModel,
                carritoViewModel = carritoViewModel,
                usuarioId = 123,
                onLogout = { cerroSesion = true },
                onNavigateToProfile = { },
                onNavigateToCart = { }
            )
        }

        composeTestRule.onNodeWithContentDescription("Cerrar Sesión").performClick()
        assert(cerroSesion)
    }

    @Test
    fun homeScreen_llamaCargarProductosAlInicio() {
        val tiendaViewModel = mockk<TiendaViewModel>(relaxed = true) {
            every { productos } returns MutableLiveData(emptyList())
            every { totalCarrito } returns MutableLiveData(0.0)
        }

        val carritoViewModel = mockk<CarritoViewModel>(relaxed = true) {
            every { total } returns MutableStateFlow(0.0)
        }

        composeTestRule.setContent {
            TiendaScreen(
                tiendaViewModel = tiendaViewModel,
                carritoViewModel = carritoViewModel,
                usuarioId = 123,
                onLogout = { },
                onNavigateToProfile = { },
                onNavigateToCart = { }
            )
        }

        verify { tiendaViewModel.cargarTodosLosProductos() }
    }
}