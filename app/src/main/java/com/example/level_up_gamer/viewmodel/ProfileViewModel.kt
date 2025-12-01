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

    /** Expone los datos del usuario actual a la UI. */
    private val _usuario = MutableLiveData<Usuario?>()
    val usuario: LiveData<Usuario?> = _usuario

    /** Expone mensajes de error a la UI. */
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    /** Indica a la UI si hay una operación de carga en progreso. */
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    /** Notifica a la UI cuando una actualización ha sido exitosa. */
    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean> = _updateSuccess

    /**
     * Carga los datos de un usuario específico desde la base de datos.
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
     * Actualiza el perfil de un usuario con los nuevos datos proporcionados.
     */
    fun actualizarPerfilCompleto(
        usuarioId: Int,
        nuevoNombre: String,
        nuevoCorreo: String,
        contrasenaActual: String,
        contrasenaNueva: String?,
        nuevaFotoPerfil: String?
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _updateSuccess.value = false

                val usuarioActual = usuarioDao.obtenerPorId(usuarioId)
                if (usuarioActual == null) {
                    _error.value = "Usuario no encontrado"
                    return@launch
                }

                // Si se intenta cambiar la contraseña, se debe verificar la actual por seguridad.
                if (!contrasenaNueva.isNullOrEmpty()) {
                    if (usuarioActual.contrasena != contrasenaActual) {
                        _error.value = "La contraseña actual es incorrecta"
                        return@launch
                    }
                }

                // Se crea un nuevo objeto de usuario con los datos actualizados.
                val usuarioActualizado = usuarioActual.copy(
                    nombre = nuevoNombre,
                    correo = nuevoCorreo,
                    contrasena = contrasenaNueva ?: usuarioActual.contrasena,
                    fotoPerfil = nuevaFotoPerfil ?: usuarioActual.fotoPerfil
                )

                usuarioDao.actualizar(usuarioActualizado)
                _usuario.value = usuarioActualizado
                _updateSuccess.value = true

            } catch (e: Exception) {
                _error.value = "Error al actualizar: ${e.message}"
                _updateSuccess.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Limpia los datos de la sesión del usuario, útil al cerrar sesión.
     */
    fun limpiarDatos() {
        _usuario.value = null
        _error.value = null
        _updateSuccess.value = false
    }
}