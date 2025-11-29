package com.example.level_up_gamer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.level_up_gamer.data.LevelUpDatabase
import com.example.level_up_gamer.model.Usuario
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    // Instancia de la base de datos y el DAO
    private val database = LevelUpDatabase.getDatabase(application)
    private val usuarioDao = database.usuarioDao()

    // LiveData para observar el resultado del login desde la Activity
    private val _usuarioLogueado = MutableLiveData<Usuario?>()
    val usuarioLogueado: LiveData<Usuario?> get() = _usuarioLogueado

    // LiveData para mostrar errores (ej: "Contraseña incorrecta")
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun login(correo: String, contrasena: String) {
        viewModelScope.launch {
            // Nota: Asegúrate de que tu UsuarioDao tenga un método que busque por correo y contraseña
            // Ojo: En tu ejemplo anterior usabas 'nombre', para un e-commerce es mejor 'correo'
            val usuario = usuarioDao.login(correo, contrasena) // Asumiendo que cambiaste el DAO a correo

            if (usuario != null) {
                _usuarioLogueado.value = usuario
                _error.value = null
            } else {
                _usuarioLogueado.value = null
                _error.value = "Credenciales incorrectas"
            }
        }
    }

    fun registroUsuario(usuario: Usuario) {
        viewModelScope.launch {
            // Verificamos si ya existe el correo (opcional pero recomendado)
            // val existe = usuarioDao.buscarPorCorreo(usuario.correo)
            // if (existe == null) { ... }

            usuarioDao.insertar(usuario)
            // Automáticamente logueamos al usuario tras el registro
            _usuarioLogueado.value = usuario
        }
    }
}