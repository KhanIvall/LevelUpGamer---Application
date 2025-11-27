package com.example.level_up_gamer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.level_up_gamer.data.LevelUpDatabase
import com.example.level_up_gamer.model.DetallePedido
import com.example.level_up_gamer.model.Pedido
import com.example.level_up_gamer.model.Producto
import kotlinx.coroutines.launch
import java.util.Date

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val db = LevelUpDatabase.getDatabase(application)
    private val usuarioDao = db.usuarioDao()

    suspend fun login(nombre: String, contrasena: String) : Boolean{
        val usuarioEncontrado = usuarioDao.login(nombre, contrasena)
        return usuarioEncontrado != null
    }
}