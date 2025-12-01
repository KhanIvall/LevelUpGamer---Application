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

    private val usuarioDao = LevelUpDatabase.getDatabase(application).usuarioDao()

    /** Expone el usuario autenticado a la UI. Null si el login falla. */
    private val _usuarioLogueado = MutableLiveData<Usuario?>()
    val usuarioLogueado: LiveData<Usuario?> get() = _usuarioLogueado

    /** Expone mensajes de error a la UI, como "Credenciales incorrectas". */
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    /**
     * Valida las credenciales de un usuario contra la base de datos.
     */
    fun login(nombre: String, contrasena: String) {
        viewModelScope.launch {
            val usuario = usuarioDao.login(nombre, contrasena)

            if (usuario != null) {
                _usuarioLogueado.value = usuario
                _error.value = null
            } else {
                _usuarioLogueado.value = null
                _error.value = "Credenciales incorrectas"
            }
        }
    }

    /**
     * Registra un nuevo usuario en la base de datos y lo autentica automáticamente.
     */
    fun registroUsuario(usuario: Usuario) {
        viewModelScope.launch {
            usuarioDao.insertar(usuario)
            // Se establece el usuario logueado automáticamente tras el registro.
            _usuarioLogueado.value = usuario
        }
    }
}