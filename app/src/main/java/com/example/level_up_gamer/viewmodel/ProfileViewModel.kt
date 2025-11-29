package com.example.level_up_gamer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.level_up_gamer.data.LevelUpDatabase
import com.example.level_up_gamer.model.Usuario
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val usuarioDao = LevelUpDatabase.getDatabase(application).usuarioDao()

    // LiveData para el usuario actual
    private val _usuario = MutableLiveData<Usuario?>()
    val usuario: LiveData<Usuario?> = _usuario

    // LiveData para errores
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    // LiveData para estado de carga
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    /**
     * Carga los datos del usuario por ID
     */
    fun cargarUsuario(usuarioId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val usuarioEncontrado = usuarioDao.obtenerPorId(usuarioId)

                if (usuarioEncontrado != null) {
                    _usuario.value = usuarioEncontrado
                } else {
                    _error.value = "Usuario no encontrado"
                }

            } catch (e: Exception) {
                _error.value = "Error al cargar datos: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Actualiza los datos del usuario
     */
    fun actualizarUsuario(usuario: Usuario) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                usuarioDao.actualizar(usuario)
                _usuario.value = usuario

            } catch (e: Exception) {
                _error.value = "Error al actualizar: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Limpia los datos del usuario (útil al cerrar sesión)
     */
    fun limpiarDatos() {
        _usuario.value = null
        _error.value = null
    }
}