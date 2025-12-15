package com.example.level_up_gamer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.level_up_gamer.remote.RetrofitInstance
import kotlinx.coroutines.launch

class PostViewModel: ViewModel() {
    val posts = MutableLiveData(emptyList<com.example.level_up_gamer.model.Post>())

    fun cargarPost(){
        viewModelScope.launch {
            val respuesta = RetrofitInstance.api.getPosts()
            posts.value = respuesta

        }
    }
}