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

    // LiveData para indicar actualización exitosa
    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean> = _updateSuccess

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
     * Actualiza el perfil completo incluyendo opcionalmente la contraseña y foto
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

                // Si quiere cambiar contraseña, verificar la actual
                if (!contrasenaNueva.isNullOrEmpty()) {
                    if (usuarioActual.contrasena != contrasenaActual) {
                        _error.value = "La contraseña actual es incorrecta"
                        return@launch
                    }
                }

                // Crear usuario actualizado
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
     * Limpia los datos del usuario (útil al cerrar sesión)
     */
    fun limpiarDatos() {
        _usuario.value = null
        _error.value = null
        _updateSuccess.value = false
    }
}